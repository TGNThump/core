/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.foundation;

import java.util.HashSet;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Module;

import uk.me.pilgrim.dev.core.events.InitEvent;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;
import uk.me.pilgrim.dev.core.util.text.Text;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class Project extends GuiceModule{
	
	public final String GROUP_ID;
	public final String ARTIFACT_ID;
	public final String NAME;
	public final String VERSION;
	
	private HashSet<Module> children;
	
	public Project(String groupId, String artifactId, String name, String version){
		this.GROUP_ID = groupId;
		this.ARTIFACT_ID = artifactId;
		this.NAME = name;
		this.VERSION = version;
		
		this.children = new HashSet<>();
	}
	
	protected Module registerChild(Module child){
		children.add(child);
		inject(child);
		install(child);
		return child;	
	}
	
	public HashSet<Module> getChildren(){
		return children;
	}
	
	public void registerChildren(EventBus events){
		getChildren().forEach((child) -> {
			events.register(child);
		});
	}
	
	@Subscribe
	public void onProjectInit(InitEvent event){
		TerraLogger.info("<h>%s v%s<r> Initialized.", Text.upperCaseFirst(NAME), VERSION);
	}
}
