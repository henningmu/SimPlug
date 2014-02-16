package org.simplug.framework.core;

import org.simplug.framework.core.config.ConfigurationManager;
import org.simplug.framework.core.pluginmanagement.PluginManager;
import org.simplug.framework.model.SimPlugContext;
import org.simplug.framework.model.events.Event;


/**
 * This the core class of the SimPlug Framework. It provides all the necessary
 * communication with other implementing frameworks. When you are using the SimPlug
 * Framework you only have to communicate with this class. Use this class as the
 * context to bind to your plugins.
 * */
public class SimPlug implements SimPlugContext {
	
	private static SimPlug instance;
	private static ConfigurationManager configManager;
	private static PluginManager pluginManager;
	
	private SimPlug() {
	}
	
	/**
	 * The core class is defined is defined as singleton. So the constructor is hidden
	 * from the outside allowing the acquisition of an instance only by calling this 
	 * method. Every call of this method is going to return the same instance.
	 * 
	 * @return the instance of the SimPlug Framework core class
	 * */
	public static SimPlug getInstance() {
		if(instance == null) {
			instance = new SimPlug();
		}
		
		return instance;
	}
	
	/**
	 * Call this method to trigger the loading of plugins. It loads the SimPlug configuration
	 * to know where the plugins are installed to. After calling this method you can send events
	 * to your plugins.
	 * */
	public void loadPlugins() {
		loadConfig();
		classifyPlugins();
	}
	
	
	/**
	 * Implementation of @see SimPlugContext.fireEvent(Event event).
	 * It takes the event and passes it to the PluginManager to handle it accordingly.
	 * 
	 * @param event
	 * 		the event to fire
	 * */
	public void fireEvent(Event event) {
		pluginManager.manageEvent(event);
	}
	
	/**
	 * Shuts down all threads and running tasks of the SimPlug Framework.
	 * Use SimPlug.isSafeShutdown to check whether it is safe to shutdown the
	 * framework without discarding events. Maybe you want to fire DestroyEvents
	 * in before to tell all plugins to shutdown.
	 * */
	public void shutdown() {
		pluginManager.shutdown();
	}
	
	/**
	 * @return true if it is safe to shutdown the framework (no pending tasks left)
	 * 		otherwise returns false
	 * */
	public boolean isSafeShutdown() {
		return pluginManager.isSafeShutdown();
	}
	
	/**
	 * Simple helper method to verify the loading state of all plugins.
	 * */
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
		pluginManager.loadAndRegisterPlugins();
	}
}
