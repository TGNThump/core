/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.modules;

import uk.me.pilgrim.dev.core.PomData;
import uk.me.pilgrim.dev.core.foundation.Project;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class CoreModule extends Project{

	public CoreModule() {
		super(PomData.ARTIFACT_ID, PomData.GROUP_ID, PomData.NAME, PomData.VERSION);
	}
	
	@Override
	protected void configure() {
		registerChild(new EventsModule());
		registerChild(new CommandsModule());
	}
}
