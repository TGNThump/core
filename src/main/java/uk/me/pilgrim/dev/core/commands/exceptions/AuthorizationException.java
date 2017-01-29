package uk.me.pilgrim.dev.core.commands.exceptions;

public class AuthorizationException extends CommandException{

	private static final long serialVersionUID = -5038624448617531757L;

	public AuthorizationException(){
		super("You do not have the required permissions to use that command!");
	}
	
	public AuthorizationException(String message) {
		super(message);
	}
	
}
