/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.modules;

import javax.inject.Singleton;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Provides;

import uk.me.pilgrim.dev.core.commands.CommandHandler;
import uk.me.pilgrim.dev.core.commands.CommandService;
import uk.me.pilgrim.dev.core.commands.arguments.BooleanArgument;
import uk.me.pilgrim.dev.core.commands.arguments.CharArgument;
import uk.me.pilgrim.dev.core.commands.arguments.EnumArgument;
import uk.me.pilgrim.dev.core.commands.arguments.NumberArgument;
import uk.me.pilgrim.dev.core.commands.arguments.StringArgument;
import uk.me.pilgrim.dev.core.config.ConfigCommands;
import uk.me.pilgrim.dev.core.events.PreInitEvent;
import uk.me.pilgrim.dev.core.foundation.GuiceModule;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class CommandsModule extends GuiceModule{
	
	@Inject
	private CommandService commands;
	
	@Subscribe
	public void onPreInit(PreInitEvent event){
		commands.addArgumentParser(new EnumArgument());
		commands.addArgumentParser(new BooleanArgument());
		commands.addArgumentParser(new CharArgument());
		commands.addArgumentParser(new NumberArgument());
		commands.addArgumentParser(new StringArgument());
	}
	
	@Provides @Singleton CommandService provideCommandService(){
		CommandService commandService = new CommandHandler();
		
		commandService.register(new ConfigCommands());
		
		return commandService;
	}
	
}
