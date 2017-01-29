package uk.me.pilgrim.dev.core.commands.exceptions;

import static com.google.common.base.Preconditions.checkNotNull;

public class InvocationCommandException extends CommandException{

	private static final long serialVersionUID = 6959454170643020091L;

    public InvocationCommandException(String message, Throwable cause) {
        super(message, checkNotNull(cause));
    }
	
}
