package org.simplug.framework.core;

import org.simplug.framework.core.config.ConfigurationManager;
import org.simplug.framework.core.pluginmanagement.PluginManager;
import org.simplug.framework.model.SimPlugContext;
import org.simplug.framework.model.events.Event;


/**
 * This the core class of the SimPlug Framework.<br />
 * It provides all the necessary communication with other implementing frameworks.
 * When you are using the SimPlug Framework you only have to communicate with this class.
 * Use this class as the context to bind to your plugins to.
 * */
public class SimPlug implements SimPlugContext {
	
	private static SimPlug instance;
	private static ConfigurationManager configManager;
	private static PluginManager pluginManager;
	
	private SimPlug() {
	}
	
	/**
	 * This core class is defined is defined as singleton. So the constructor is hidden
	 * from the outside allowing the acquisition of an instance only by calling this 
	 * method. Every call of this method is returning the same instance.
	 * 
	 * @return
	 * 		the instance of the SimPlug Framework core class.
	 * */
	public static SimPlug getInstance() {
		if(instance == null) {
			instance = new SimPlug();
		}
		
		return instance;
	}
	
	/**
	 * Call this method to trigger the loading of plugins. It loads the SimPlug configuration
	 * to know where the plugins are installed to. Only after calling this method you can
	 * send events to your plugins.
	 * */
	public void loadPlugins() {
		loadConfig();
		classifyPlugins();
	}
	
	
	/**
	 * Implementation of {@link org.simplug.framework.model.SimPlugContext#fireEvent(Event)}.
	 * It takes the event and passes it to the PluginManager to handle it accordingly.
	 * 
	 * @param event
	 * 		the event to fire.
	 * */
	public void fireEvent(Event event) {
		pluginManager.manageEvent(event);
	}
	
	/**
	 * Shuts down all threads and running tasks of the SimPlug Framework.<br />
	 * Use {@link #isSafeShutdown()} to check whether it is safe to shutdown the
	 * framework without discarding events. Maybe you want to fire
	 * {@link org.simplug.framework.model.events.DestroyEvent} to your plugins
	 * to tell them to shutdown.
	 * */
	public void shutdown() {
		pluginManager.shutdown();
	}
	
	/**
	 * Returns whether it is safe call {@link #shutdown()} or not.<br />
	 * Returns <code>true</code> when there are no pending events left (the plugins may still
	 * be working on some tasks though) or <code>false</code> if there are still pending events.
	 * 
	 * @return
	 * 		<code>true</code> if there are no pending events, otherwise returns <code>false</code>.
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
