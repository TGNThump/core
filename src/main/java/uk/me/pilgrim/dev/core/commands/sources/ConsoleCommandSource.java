/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.commands.sources;

import uk.me.pilgrim.dev.core.util.logger.TerraLogger;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class ConsoleCommandSource implements CommandSource {
	
	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.sources.CommandSource#hasPermission(java.lang.String)
	 */
	@Override
	public boolean hasPermission(String perm) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.sources.CommandSource#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String of) {
		TerraLogger.info(of);
	}
	
}
