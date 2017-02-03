/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core;

import java.util.HashMap;
import java.util.HashSet;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

import uk.me.pilgrim.dev.core.config.CoreConfig;
import uk.me.pilgrim.dev.core.events.InitEvent;
import uk.me.pilgrim.dev.core.foundation.Project;
import uk.me.pilgrim.dev.core.modules.CoreModule;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;
import uk.me.pilgrim.dev.core.util.text.Text;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class Core{
	
	private static HashMap<Class<? extends Project>, Project> projects = new HashMap<>();
	
	private static Injector injector;
	private static boolean devMode = false;
	private EventBus events;
	private static CoreConfig config;
	
	@Inject
	private Core(EventBus events){
		this.events = events;
		
		events.register(this);
		config.registerEvents();
		
		for (Project project : projects.values()){
			events.register(project);
			injector.injectMembers(project);
			project.registerChildren(events);
		}
		
		events.post(new InitEvent());
	}
	
	public void fireEvent(Object event){
		events.post(event);
	}
	
	// Static
	
	public static void registerCoreModules() {
		register(new CoreModule());
	}
	
	public static void register(Project project){
		projects.put(project.getClass(), project);
		TerraLogger.debug("Module <h>%s<r> Registered.", Text.upperCaseFirst(project.NAME));
	}

	public static void buildInjector() {
		
		HashSet<Module> modules = new HashSet<>();
		modules.addAll(projects.values());
		for(Project p : projects.values()){
			modules.addAll(p.getChildren());
		}
		
		injector = Guice.createInjector(getStage(), modules);
		TerraLogger.debug("Injector Created.");
	}
	
	public static Injector getInjector(){
		if (injector == null) buildInjector();
		return injector;
	}
	
	public static <T> T inject(T object){
		getInjector().injectMembers(object);
		return object;
	}

	public static void loadConfig() {
		config = new CoreConfig();
		devMode = config.devMode;
	}
	
	private static Stage getStage(){
		return devMode ? Stage.DEVELOPMENT : Stage.PRODUCTION;
	}

	public static Core init() {
		return injector.getInstance(Core.class);		
	}

	public static boolean isDevMode() {
		return devMode;
	}
	
	public static <E> E get(Class<E> name){
		return injector.getInstance(name);
	}
	
	public static class Events{
		public static void fire(Object event){
			Core.get(EventBus.class).post(event);
		}
	}
}
