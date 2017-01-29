/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.modules;

import uk.me.pilgrim.dev.core.commands.CommandHandler;
import uk.me.pilgrim.dev.core.commands.MethodCommandService;
import uk.me.pilgrim.dev.core.commands.arguments.BooleanArgument;
import uk.me.pilgrim.dev.core.commands.arguments.CharArgument;
import uk.me.pilgrim.dev.core.commands.arguments.EnumArgument;
import uk.me.pilgrim.dev.core.commands.arguments.NumberArgument;
import uk.me.pilgrim.dev.core.commands.arguments.StringArgument;
import uk.me.pilgrim.dev.core.foundation.GuiceModule;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class CommandsModule extends GuiceModule{
	
	@Override
	protected void configure() {
		MethodCommandService commandService = new CommandHandler();
		
		commandService.addArgumentParser(new EnumArgument());
		commandService.addArgumentParser(new BooleanArgument());
		commandService.addArgumentParser(new CharArgument());
		commandService.addArgumentParser(new NumberArgument());
		commandService.addArgumentParser(new StringArgument());
		
		bind(MethodCommandService.class).toInstance(commandService);

	}
	
}