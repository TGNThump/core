/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.config;

import ninja.leaping.configurate.objectmapping.Setting;
import uk.me.pilgrim.dev.core.config.Config;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class CoreConfig extends Config{

	public CoreConfig(){
		super("core.conf");
	}
	
	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.config.Config#setDefaults()
	 */
	@Override
	public void setDefaults() {
		devMode = setDefault(devMode, false);
	}
	
	@Setting
	public boolean devMode;
	
}
