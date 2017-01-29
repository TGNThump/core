/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.modules;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import uk.me.pilgrim.dev.core.foundation.GuiceModule;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class EventsModule extends GuiceModule{
	
	@Override
	protected void configure() {
		bind(EventBus.class).toInstance(new EventBus());
	}
	
//	@Subscribe
//	public void onEvent(Object event){
//		TerraLogger.debug("Event: " + event.getClass().getSimpleName());
//	}
	
	@Subscribe
	public void onEvent(DeadEvent event){
		TerraLogger.debug("DEAD EVENT: " + event.getEvent().getClass().getSimpleName());
	}
	
}
