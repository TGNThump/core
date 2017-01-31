package uk.me.pilgrim.dev.core.commands;

import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.arguments.ArgumentParser;
import uk.me.pilgrim.dev.core.commands.exceptions.CommandException;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.foundation.Project;
import uk.me.pilgrim.dev.core.util.Context;

/**
 * A service that provides the ability to register methods in an object as {@link Command}.
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public interface CommandService {
	
	/**
	 * Registers all {@link Command} methods in this instance.
	 * @param plugin The {@link Project} to register the commands to.
	 * @param handler The method to register.
	 * @throws IllegalArgumentException if the parameter "plugin" is not a {@link Plugin}!
	 */
	public void register(Object handler) throws IllegalArgumentException;

	/**
	 * Registers an {@link ArgumentParser}, later added parsers with have a higher priority.
	 * @param plugin The {@link Plugin} to register the commands to.
	 * @param parser The {@link ArgumentParser} to register.
	 * @throws IllegalArgumentException if the parameter "plugin" is not a {@link Plugin}!
	 */
	public void addArgumentParser(ArgumentParser parser) throws IllegalArgumentException;
	
	/**
	 * Returns the highest priority {@link ArgumentParser} that supports this class-type.
	 * @param type The argument type.
	 * @return The highest priority {@link ArgumentParser} that supports this class-type.
	 * @throws IllegalArgumentException if the parameter "plugin" is no {@link Plugin}!
	 */
	public ArgumentParser getArgumentParser(Class<?> type) throws IllegalArgumentException;
	
	public CommandResult processCommand(Context context, String command) throws CommandException, Throwable;
	
	public CommandResult processCommand(CommandSource source, String command) throws CommandException, Throwable;
	
}
