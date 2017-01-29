package uk.me.pilgrim.dev.core.commands.exceptions;

public class CommandException extends Exception{
	
	private static final long serialVersionUID = -1391484198599433287L;

	private String message;
	
	public CommandException(String string) {
		this.message = string;
	}
	
	public CommandException(String string, Throwable cause){
		super(cause);
		this.message = string;
	}
	
	public String getDescription() {
		return message;
	}
	
}
