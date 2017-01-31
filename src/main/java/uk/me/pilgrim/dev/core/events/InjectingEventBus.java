package uk.me.pilgrim.dev.core.events;

import com.google.common.eventbus.EventBus;

import uk.me.pilgrim.dev.core.Core;

public class InjectingEventBus extends EventBus{

	public InjectingEventBus(EventsExceptionHandler eventsExceptionHandler) {
		super(eventsExceptionHandler);
	}

	public void register(Object object) {
		Core.inject(object);
		super.register(object);
	}
	
}
