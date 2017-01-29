/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.util.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class Text {
	
	private Text(){}
	
	protected static final int PAGEHEIGHT_CONSOLE = 50;
	protected static final Map<String, String> consoleParseReplacements = new HashMap<String, String>();

	protected static final Pattern consolePattern;
	
	protected static void addParseReplacement(String replacement, String... keys){
		Arrays.asList(keys).forEach((key) -> {
			consoleParseReplacements.put(key, replacement);
		});
		
	}
	
	static {
		addParseReplacement(ConsoleColor.BLACK, "<black>");
		
		addParseReplacement(ConsoleColor.BLUE, "<blue>");
		addParseReplacement(ConsoleColor.CYAN, "<cyan>", "<l>", "<k>", "<c>", "<p>");
		addParseReplacement(ConsoleColor.GREEN, "<green>", "<g>");
		addParseReplacement(ConsoleColor.MAGENTA, "<magenta>");
		addParseReplacement(ConsoleColor.RED, "<red>", "<b>", "<v>");
		addParseReplacement(ConsoleColor.WHITE, "<white>");
		addParseReplacement(ConsoleColor.YELLOW, "<yellow>", "<h>");
		
		addParseReplacement(ConsoleColor.RESET, "<reset>", "<r>");
		
		StringBuilder consolePatternStringBuilder = new StringBuilder();
		
		consoleParseReplacements.keySet().forEach((find)->{
			consolePatternStringBuilder.append("(");
			consolePatternStringBuilder.append(Pattern.quote(find));
			consolePatternStringBuilder.append(")|");
		});
		
		String consolePatternString = consolePatternStringBuilder.toString();
		consolePatternString = consolePatternString.substring(0, consolePatternString.length() - 1);
		consolePattern = Pattern.compile(consolePatternString);
	}
	
	public static String of(String string){
		StringBuffer ret = new StringBuffer();
		Matcher matcher = consolePattern.matcher(string);
		
		while (matcher.find()){
			matcher.appendReplacement(ret, consoleParseReplacements.get(matcher.group(0)));
		}
		matcher.appendTail(ret);
		return ret.toString();
	}
	
	public static String of(String string, Object... args){
		Object[] formattedArgs = new Object[args.length];
		for(int i = 0; i < args.length; i ++){
			Object arg = args[i];
			if (arg instanceof String){
				formattedArgs[i] = of((String) arg);
			} else {
				formattedArgs[i] = arg;
			}
		}
		return String.format(of(string), formattedArgs); 
	}
	
	public static ArrayList<String> of(Collection<String> strings){
		ArrayList<String> ret = new ArrayList<String>(strings.size());
		strings.forEach((string)->{
			ret.add(of(string));
		});
		return ret;
	}
	
	// Cases
	
	public static String upperCaseFirst(String string){
		if (string == null) return null;
		if (string.length() == 0) return string;
		return string.substring(0,1).toUpperCase() + string.substring(1);
	}

	// Implode
	
	public static String implode(final Object[] list, final String glue, final String format) {
		StringBuilder ret = new StringBuilder();
		
		for (int i = 0; i < list.length; i++) {
			Object item = list[i];
			String str = item == null ? "NULL" : item.toString();
			
			if (i != 0) {
				ret.append(glue);
			}
			if (format != null) {
				ret.append(String.format(format, str));
			} else {
				ret.append(str);
			}
		}
		
		return ret.toString();
	}
	
	public static String implode(final Object[] list, final String glue) {
		return implode(list, glue, null);
	}
	
	public static String implode(final Collection<? extends Object> coll, final String glue, String format) {
		return implode(coll.toArray(new Object[0]), glue, format);
	}
	
	public static String implode(final Collection<? extends Object> coll, final String glue) {
		return implode(coll, glue, null);
	}
	
	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String format, final String comma, final String and, final String dot) {
		if (objects.size() == 0)
			return "";
		if (objects.size() == 1)
			return implode(objects, comma, format);
		
		List<Object> ourObjects = new ArrayList<Object>(objects);
		
		String lastItem = ourObjects.get(ourObjects.size() - 1).toString();
		String nextToLastItem = ourObjects.get(ourObjects.size() - 2).toString();
		if (format != null) {
			lastItem = String.format(format, lastItem);
			nextToLastItem = String.format(format, nextToLastItem);
		}
		String merge = nextToLastItem + and + lastItem;
		ourObjects.set(ourObjects.size() - 2, merge);
		ourObjects.remove(ourObjects.size() - 1);
		
		return implode(ourObjects, comma, format) + dot;
	}
	
	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String comma, final String and, final String dot) {
		return implodeCommaAndDot(objects, null, comma, and, dot);
	}
	
	public static String implodeCommaAnd(final Collection<? extends Object> objects, final String comma, final String and) {
		return implodeCommaAndDot(objects, comma, and, "");
	}
	
	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String color) {
		return implodeCommaAndDot(objects, color + ", ", color + " and ", color + ".");
	}
	
	public static String implodeCommaAnd(final Collection<? extends Object> objects, final String color) {
		return implodeCommaAndDot(objects, color + ", ", color + " and ", "");
	}
	
	public static String implodeCommaAndDot(final Collection<? extends Object> objects) {
		return implodeCommaAndDot(objects, "");
	}
	
	public static String implodeCommaAnd(final Collection<? extends Object> objects) {
		return implodeCommaAnd(objects, "");
	}
}
