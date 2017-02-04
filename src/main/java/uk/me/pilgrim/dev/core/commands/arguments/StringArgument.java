package uk.me.pilgrim.dev.core.commands.arguments;

import uk.me.pilgrim.dev.core.commands.exceptions.ArgumentException;

public class StringArgument implements ArgumentParser{

	@Override
	public boolean isTypeSupported(Class<?> type) {
		return type == String.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T parseArgument(Class<T> type, String arg) throws ArgumentException, IllegalArgumentException {
		checkTypeSupported(type);
		
		if (arg.startsWith("'") || arg.startsWith("\"")) return (T) arg.substring(1, arg.length()-1);
		return (T) arg;
	}
	
	@Override
	public int getArgumentEnd(String arguments){
		if (arguments.startsWith("'")) return arguments.substring(1).indexOf("'")+2;
		if (arguments.startsWith("\"")) return arguments.substring(1).indexOf("\"")+2;
		return arguments.indexOf(' ');
	}
	
}
