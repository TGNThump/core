/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.config;

import java.io.File;
import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.events.ConfigurationReloadEvent;
import uk.me.pilgrim.dev.core.events.ConfigurationSaveEvent;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public abstract class Config {
	
	private File file;
	private CommentedConfigurationNode config;
	
	private ObjectMapper<Config>.BoundInstance configMapper;
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	
	public void registerEvents(){
		Core.get(EventBus.class).register(this);
	}
	
	public Config(String folder, String configName){
		folder = folder + "/";
		
		if (!new File(folder).isDirectory()){
			new File(folder).mkdirs();
		}
		
		file = new File(folder + configName);
		
		create();
		init();
		load();
		setDefaults();
		save();
	}
	
	public Config(String configName){
		String folder = "config/";
		
		if (!new File(folder).isDirectory()){
			new File(folder).mkdirs();
		}
		
		file = new File(folder + configName);
		
		create();
		init();
		load();
		setDefaults();
		save();
	}
	
	public Config(){
		String folder = "config/";
		
		if (!new File(folder).isDirectory()){
			new File(folder).mkdirs();
		}
		
		file = new File(folder + "config.conf");
		
		create();
		init();
		load();
		setDefaults();
		save();
	}
	
	@Subscribe
	public void reload(ConfigurationReloadEvent event){
		reload();
	}
	
	@Subscribe
	public void save(ConfigurationSaveEvent event){
		save();
	}
	
	public void reload(){
		create();
		load();
		setDefaults();
		save();
	}
	
	private void init(){
		try{
			this.configMapper = ObjectMapper.forObject(this);
		} catch (ObjectMappingException e){
			e.printStackTrace();
		}
	}
	
	private void create(){
		if (!file.exists()){
			TerraLogger.info("Creating new <h>" + file.getName() + "<r> file...");
			try {
				file.createNewFile();
			} catch (IOException e) {
				TerraLogger.info("<b>Failed to create new config file named <h>" + file.getName() + "<b>.");
				e.printStackTrace();
			}
		}
	}
	
	public void save(){
		try {
			SimpleConfigurationNode out = SimpleConfigurationNode.root();
			this.configMapper.serialize(out);
			this.loader.save(out);
		} catch (ObjectMappingException |IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(){
		this.loader = HoconConfigurationLoader.builder().setFile(file).build();
		try {
			config = this.loader.load();
			this.configMapper.populate(config);
		} catch (ObjectMappingException | IOException e) {
			TerraLogger.error("<b>Failed to load config file named <h>" + file.getName() + "<b>.");
			e.printStackTrace();
		}
	}
	
	public abstract void setDefaults();
	
	public <T> T setDefault(T param, T value){
		if (param == null) return value;
		return param;
	}
	
	@ConfigSerializable
	public static class Category {
		
		private void injectConfig(){
			Core.getInjector().injectMembers(this);
		}
		
		protected Category(){
			injectConfig();
		}
		
    }
	
}
