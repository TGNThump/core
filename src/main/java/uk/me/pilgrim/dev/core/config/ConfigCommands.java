/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.config;

import com.google.common.eventbus.EventBus;

import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.events.ConfigurationReloadEvent;
import uk.me.pilgrim.dev.core.util.Context;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class ConfigCommands {
	
	@Command("config")
	public CommandResult onConfig(Context context){
		return CommandResult.FAILURE;
	}
	
	@Command("config reload")
	public CommandResult onConfigReload(Context context){
		Core.get(EventBus.class).post(new ConfigurationReloadEvent());
		context.get(CommandSource.class).sendMessage("Configuration Reloaded.");
		return CommandResult.SUCCESS;
	}
}
