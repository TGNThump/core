package uk.me.pilgrim.dev.core.events;

import com.google.common.eventbus.SubscriberExceptionContext;

public class ExceptionEvent extends CancelableEvent{
	
	private final Throwable exception;
	private final SubscriberExceptionContext context;
	
	public ExceptionEvent(Throwable exception, SubscriberExceptionContext context){
		this.exception = exception;
		this.context = context;
	}
	
	public Throwable getException(){
		return this.exception;
	}
	
	public SubscriberExceptionContext getContext(){
		return context;
	}
	
	public Object getEvent(){
		return context.getEvent();
	}
}
