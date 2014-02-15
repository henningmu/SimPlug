package org.simplug.framework.core;

import org.simplug.framework.core.config.ConfigurationManager;
import org.simplug.framework.core.pluginmanagement.PluginManager;
import org.simplug.framework.model.SimPlugContext;
import org.simplug.framework.model.events.Event;

public class SimPlug implements SimPlugContext {
	
	private static SimPlug instance;
	private static ConfigurationManager configManager;
	private static PluginManager pluginManager;
	
	private SimPlug() {
		
	}
	
	public static SimPlug getInstance() {
		if(instance == null) {
			instance = new SimPlug();
		}
		
		return instance;
	}
	
	public void loadPlugins() {
		loadConfig();
		classifyPlugins();
	}
	
	public void fireEvent(Event event) {
		pluginManager.manageEvent(event);
	}
	
	public void shutdown() {
		pluginManager.shutdown();
	}
	
	public boolean isSafeShutdown() {
		return pluginManager.isSafeShutdown();
	}
	
	@Deprecated
	public void logLoadedPlugins() {
		pluginManager.logPlugins();
	}
	
	private void loadConfig() {
		configManager = ConfigurationManager.getInstance();
		configManager.loadConfig();
	}
	
	private void classifyPlugins() {
		pluginManager = new PluginManager();
		pluginManager.loadAndClassifyPlugins();
	}
}
