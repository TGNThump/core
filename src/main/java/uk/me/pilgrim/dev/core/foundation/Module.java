/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.foundation;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.commands.CommandService;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class Module extends GuiceModule{
	
	@Inject
	protected CommandService commands;
	
	@Inject
	protected EventBus events;
	
	protected void registerCommands(Object handler){
		commands.register(Core.inject(handler));
	}
	
	protected void registerEvents(Object handler){
		events.register(handler);
	}
}
