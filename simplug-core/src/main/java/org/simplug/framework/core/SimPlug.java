package org.simplug.framework.core;

import org.simplug.framework.core.config.ConfigurationManager;

public class SimPlug {
	
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
	
	// only for testing
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
