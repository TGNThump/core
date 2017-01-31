package uk.me.pilgrim.dev.core.events;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class EventsExceptionHandler implements SubscriberExceptionHandler{

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		Object event = context.getEvent();
		if (context.getEvent() instanceof DeadEvent) event = ((DeadEvent) context.getEvent()).getEvent();
		
		if (event instanceof ExceptionEvent){
			exception.printStackTrace();
			return;
		}		
		ExceptionEvent e = new ExceptionEvent(exception, context);
		context.getEventBus().post(e);
		if (e.isCanceled()) return;
		exception.printStackTrace();
	}
}
