package uk.me.pilgrim.dev.core.commands;

import static com.google.common.base.Preconditions.checkNotNull;
import static uk.me.pilgrim.dev.core.util.Conditions.notNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import uk.me.pilgrim.dev.core.commands.annotations.Alias;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.annotations.Desc;
import uk.me.pilgrim.dev.core.commands.annotations.Help;
import uk.me.pilgrim.dev.core.commands.annotations.Perm;
import uk.me.pilgrim.dev.core.commands.annotations.Usage;
import uk.me.pilgrim.dev.core.commands.arguments.ArgumentParser;
import uk.me.pilgrim.dev.core.commands.exceptions.ArgumentException;
import uk.me.pilgrim.dev.core.commands.exceptions.AuthorizationException;
import uk.me.pilgrim.dev.core.commands.exceptions.CommandException;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.core.util.text.Text;

/**
 * A {@link Method} wrapper that collects information about a {@link Command} method.
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class MethodCommand {
	
	private final Object handler;
	private final Method method;
	
	private final CommandHandler commandHandler;
	
	private String[] path;
	
	private String primary;
	private Optional<MethodCommand> parent = Optional.empty();
	private HashMap<String, MethodCommand> children = Maps.newHashMap();
	private List<String> alias = Lists.newArrayList();
	private List<String> perms = Lists.newArrayList();
	private Optional<String> desc = Optional.empty();
	private Optional<String> help = Optional.empty();
	private Optional<String> usage = Optional.empty();
	
	private List<Parameter> params = Lists.newArrayList();
	private HashMap<String, Parameter> flags = Maps.newHashMap();
		
	public MethodCommand(Object handler, Method method, CommandHandler commandHandler){
		checkNotNull(handler, "handler");
		checkNotNull(method, "method");
		checkNotNull(commandHandler, "commandHandler");
		this.handler = handler;
		this.method = method;
		this.commandHandler = commandHandler;
		
		Command definition = method.getAnnotation(Command.class);
		if (definition == null || !method.getReturnType().isAssignableFrom(CommandResult.class)){
			throw new IllegalArgumentException();
		}
		
		Desc desc = method.getAnnotation(Desc.class);
		Help help = method.getAnnotation(Help.class);
		Usage usage = method.getAnnotation(Usage.class);
		Perm[] perms = method.getAnnotationsByType(Perm.class);
		Alias[] alias = method.getAnnotationsByType(Alias.class);
		
		this.path = definition.value().split(" ", -1);
		this.primary = path[path.length - 1];
		this.alias.add(primary);
		
		String parentPath = getParentPath();
		
		if (!parentPath.isEmpty()){
			if (commandHandler.hasCommand(parentPath)){
				MethodCommand parent = commandHandler.getCommand(parentPath);
				this.setParent(parent);
			} else {
				throw new InvalidParameterException("Command '" + method.toString() + "' Registered before Parent.");
			}
		}
		
		if (notNull(desc)) this.desc = Optional.of(desc.value());
		if (notNull(help)) this.help = Optional.of(help.value());
		if (notNull(usage)) this.usage = Optional.of(usage.value());
		
		for (int i = 0; i < perms.length; i++){
			this.perms.add(perms[i].value());
		}
		
		for (int i = 0; i < alias.length; i++){
			this.alias.add(alias[i].value());
		}
		
		if (!method.getParameters()[0].getType().isAssignableFrom(Context.class)){
			throw new IllegalArgumentException();
		}
		
		for (java.lang.reflect.Parameter param : method.getParameters()){
			if (param.getType().isAssignableFrom(Context.class)) continue;
			Parameter parameter = new Parameter(this, commandHandler, param);
			
			if (!parameter.isValid()) throw new IllegalArgumentException();
			
			this.params.add(parameter);
			if (parameter.isFlag()){
				parameter.getAliases().forEach(a -> {
					this.flags.put(a, parameter);
				});
			}
		}
	}
	
	public List<String> getSuggestions(Context context, String args){

		List<String> suggestions = Lists.newArrayList();
		
		try{
			parseArgs(context, args);
		} catch (ArgumentException e){
			while (e != null){
				if (e instanceof ArgumentException.NotEnoughArgumentsException){
					ArgumentParser ap = commandHandler.getArgumentParser(e.getExpectedType());
					if (args.endsWith(" ") || args.isEmpty()) suggestions.addAll(ap.suggestArgs(e.getExpectedType(), ""));
				} else if (e instanceof ArgumentException.TooManyArgumentsException){
				} else {
					ArgumentParser ap = commandHandler.getArgumentParser(e.getExpectedType());
					for (String s : ap.suggestArgs(e.getExpectedType(), e.getWrongArgument())){
						suggestions.add(s);
					}
				}
				
				e = e.getAnother();
			}
		} catch (AuthorizationException e) {
		} finally {
			params.forEach(p -> {p.resetValue();});
		}
		
		if (suggestions.size() > 20) suggestions.subList(20, suggestions.size()).clear();
		return suggestions;
		
	}
		
	public CommandResult execute(Context context, String args) throws CommandException{

		List<Object> cmdParams;
		CommandSource source = context.get(CommandSource.class);
		try {
			cmdParams = parseArgs(context, args);
			params.forEach(p -> {p.resetValue();});
		} catch (ArgumentException ex) {
			params.forEach(p -> {p.resetValue();});
			throw ex;
		} catch (AuthorizationException e) {
			source.sendMessage("You do not have permission to execute this command!");
			return CommandResult.FAILURE;
		}
		
		boolean p = getPerms().isEmpty();
		for (String perm : getPerms()){
			if (source.hasPermission(perm)) p = true;
		}
		
		if (!p){
			source.sendMessage("You do not have permission to execute this command!");
			return CommandResult.FAILURE;
		}
		
		
		
		try {
			return (CommandResult) method.invoke(handler, cmdParams.toArray());
		} catch (Exception e){
			if (e instanceof InvocationTargetException){
				Throwable c = e.getCause();
				if (c == null) c = e;
				
				String reply = "";
				StackTraceElement[] stackTrace = c.getStackTrace();
				reply += c;
				reply += "\n";
				for (StackTraceElement s : stackTrace){
					reply += " at " + s;
					
					reply += "\n";
				}			
				
				source.sendMessage("An error occoured while trying to execute this command.");
				source.sendMessage("```" + reply + "```");
				c.printStackTrace();
			} else {
				String reply = "";
				StackTraceElement[] stackTrace = e.getStackTrace();
				reply += e;
				reply += "\n";
				for (StackTraceElement s : stackTrace){
					reply += " at " + s;
					
					reply += "\n";
				}
								
				source.sendMessage("An error occoured while trying to execute this command.");
				source.sendMessage("```" + reply + "```");
				e.printStackTrace();
			}			
		}
		return CommandResult.FAILURE;
	}
	
	private List<Object> parseArgs(Context context, String args) throws ArgumentException, AuthorizationException{		
		List<Object> parsedArgs = Lists.newArrayList();
		parsedArgs.add(context);
			
		for (Parameter param : getParameters()){
			if (param.isFlag()) continue;
			
			ArgumentParser ap = commandHandler.getArgumentParser(param.getType());
			
			while (true){
				while (args.startsWith(" ")) args = args.substring(1);
				
				if (args.startsWith("-")){
					String flag;
					if (args.indexOf(" ") > 0){
						flag = args.substring(1, args.indexOf(" "));
						args = args.substring(args.indexOf(" "));
					} else {
						flag = args.substring(1);
						args = "";
					}

					if (flags.containsKey(flag)){
						args = flags.get(flag).parse(context, args);
					} else throw new ArgumentException("'" + flag + "' is not a valid flag for this command.", flag, null, null);
					continue;
				}
				
				if (args.isEmpty()){
					if (!param.isOptional()){
						throw new ArgumentException.NotEnoughArgumentsException("Not enough arguments!", ap, param.getType());
					}
					break;
				}
				
				args = param.parse(context, args);
				if (!param.isVarArgs()) break;
			}
		}
		
		while (!args.isEmpty()){
			while (args.startsWith(" ")) args = args.substring(1);
			
			if (args.startsWith("-")){
				String flag;
				if (args.indexOf(" ") > 0){
					flag = args.substring(1, args.indexOf(" "));
					args = args.substring(args.indexOf(" "));
				} else {
					flag = args.substring(1);
					args = "";
				}

				if (flags.containsKey(flag)){
					args = flags.get(flag).parse(context, args);
				} else throw new ArgumentException("'" + flag + "' is not a valid flag for this command.", flag, null, null);
				continue;
			}
			
			throw new ArgumentException.TooManyArgumentsException("Too many arguments!", args);
		}
		
		for (Parameter param : getParameters()){
			parsedArgs.add(param.getValue());
		}
		
		return parsedArgs;
	}
	
	/**
	 * The usage override for this command.
	 * @return The usage override
	 */
	public Optional<String> getUsageOverride() {
		return usage;
	}
	
	public String getUsage(CommandSource source){
		if (usage.isPresent()) return usage.get();
		
		String ret = getUsagePart(source);
		
		
		boolean first = true;
		for (Parameter param : getParameters()){
			if (first) first = false;
			else ret += " ";
			ret += (param.getUsage(source));
		}
		
		return ret;
	}
	
	protected String getUsagePart(CommandSource source){
		String ret = "";
		if (this.parent.isPresent()) ret += (parent.get().getUsagePart(source));
		
		ret += primary;
		
//		builder.append(Text
//				.builder(primary)
//				.color(TextColors.DARK_GRAY)
//				.onClick(TextActions.suggestCommand("/" + MyText.implode(path, " ") + " "))
//				.onHover(TextActions.showText(getUsageHover(source)))
//				.toText()
//			);
		
		ret += " ";
		
		return ret;
	}
	
	protected String getUsageHover(CommandSource source){
		String ret = "";
		
		for (int i = 0; i < alias.size(); i++){
			ret += alias.get(i);
			if (i < alias.size() - 1) ret += " | ";
		}
		
		if (desc.isPresent()){
			ret += "\n";
			ret += desc.get();
		}
		
		return ret;
	}
	
	public int getDepth(){
		if (!parent.isPresent()) return 0;
		return (parent.get().getDepth() + 1);
	}
	
	public MethodCommand getChild(String[] args){
		if (children.containsKey(args[0])){
			return children.get(args[0]).getChild(Arrays.copyOf(args, args.length-1));
		}
		return this;
	}
	
	protected void registerChild(MethodCommand child){
		checkNotNull(child);
		for (String a: child.getAliases()){
			children.put(a, child);
		}
	}
	
	protected void removeChild(MethodCommand child){
		checkNotNull(child);
		children.remove(child);
	}
	
	public MethodCommand setParent(MethodCommand parent){
		if (this.parent.isPresent()) this.parent.get().removeChild(this);
		this.parent = Optional.of(parent);
		parent.registerChild(this);
		return this;
	}
	
	/**
	 * Get the {@link Method} this object wraps.
	 * @return The wrapped method
	 */
	public Method getMethod() {
		return method;
	}
	
	/**
	 * Get a list of {@link Parameter}s for this method.
	 * @return A list of the methods parameters.
	 */
	public List<Parameter> getParameters() {
		return params;
	}
	
	/**
	 * Get a map of strings to flag {@link Parameter}s for this method.
	 * @return A map of strings to flag parameters.
	 */
	public Map<String, Parameter> getFlags() {
		return flags;
	}
	
	/**
	 * Evaluates whether a flag is present matching a key.
	 * @param key The flag name.
	 * @return Whether the flag is present.
	 */
	public boolean hasFlag(String key){
		return flags.containsKey(key);
	}
	
	/**
	 * Get the path of this command.
	 * @return The command path
	 */
	public String[] getPath() {
		return path;
	}
	
	public boolean hasParent(){
		return parent.isPresent();
	}

	/**
	 * Get the primary alias of this command.
	 * @return The primary alias
	 */
	public String getPrimary() {
		return primary;
	}

	/**
	 * Get a list of all aliases (including the primary) for this command.
	 * @return A list of aliases.
	 */
	public List<String> getAliases() {
		return alias;
	}

	/**
	 * Get a list of permissions required to execute this command.
	 * @return A list of permissions.
	 */
	public List<String> getPerms() {
		return perms;
	}

	/**
	 * The description of this command.
	 * @return The description
	 */
	public Optional<String> getDesc() {
		return desc;
	}

	/**
	 * The help message for this command.
	 * @return The help message
	 */
	public Optional<String> getHelp() {
		return help;
	}
	
	public String getParentPath(){
		String path = Text.implode(this.path, " ");
		if (path.contains(" "))
			return path.substring(0, path.lastIndexOf(" "));
		return "";
	}
	
	public List<String> getAliasPaths(){
		List<String> paths = Lists.newArrayList();
		if (parent.isPresent()){
			for (String start : parent.get().getAliasPaths()){
				for (String end : getAliases()){
					paths.add(start + " " + end);
				}
			}
		} else {
			for (String end : getAliases()){
				paths.add(end);
			}
		}
		
		return paths;
	}
}
