package uk.me.pilgrim.dev.core.commands.arguments;

import uk.me.pilgrim.dev.core.commands.exceptions.ArgumentException;

/**
 * A {@link Character} {@link ArgumentParser}
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class CharArgument implements ArgumentParser {

	@Override
	public boolean isTypeSupported(Class<?> type) {
		return type == Character.class || type == char.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T parseArgument(Class<T> type, String arg) throws ArgumentException, IllegalArgumentException {
		checkTypeSupported(type);
		
		if (arg.length() == 1){
			return (T) new Character(arg.charAt(0));
		}
		
		throw getArgumentException(type, arg);
	}
	
	@Override
	public String getArgumentTypeName(Class<?> type) throws IllegalArgumentException {
		checkTypeSupported(type);
		return "Character";
	}
}
