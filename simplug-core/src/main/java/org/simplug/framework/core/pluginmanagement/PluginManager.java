package org.simplug.framework.core.pluginmanagement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.simplug.framework.core.config.Configuration;
import org.simplug.framework.core.config.ConfigurationManager;
import org.simplug.framework.core.util.AnnotationBasedPluginLoader;
import org.simplug.framework.core.util.ConfigBasedPluginLoader;
import org.simplug.framework.core.util.PluginLoader;
import org.simplug.framework.model.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for managing all plugins deployed with the SimPlug Framework.<br />
 * It manages all incoming events and delegates them to the receiving plugins.
 * It functions as kind of "uber-manager" for all the other managers like {@link EventQueueManager} or similar.
 * */
public class PluginManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(PluginManager.class);

	private LinkedHashMap<String, List<Class<?>>> eventListeners;
	private LinkedBlockingQueue<Event> eventQueue;
	private EventQueueManager eventQueueManager;

	/**
	 * Simple constructor initializing all needed members.
	 * It does NOT load or start any plugins.
	 * */
	public PluginManager() {
		eventQueue = new LinkedBlockingQueue<Event>();
	}

	/**
	 * This method is responsible for loading all plugins and registering them for later access.
	 * It reads the SimPlug configuration and uses a {@link org.simplug.framework.core.util.PluginLoader} to load all the plugins and
	 * save them in a specific structure.<br />
	 * This method only loads and registers the plugins. It neither initializes them nor starts them in any way.
	 * */
	public void loadAndRegisterPlugins() {
		ConfigurationManager configurationManager = ConfigurationManager
				.getInstance();
		String pluginPath = configurationManager.getProperty(Configuration.CONF_PLUGIN_DIRECTORY);
		String configName = configurationManager.getProperty(Configuration.CONF_PLUGIN_CONFIG_NAME);
		String loadingMethod = configurationManager.getProperty(Configuration.CONF_PLUGIN_LOADING_METHOD);
		
		PluginLoader pluginLoader = null;
		if(Configuration.PLUGIN_LOADING_CONFIG_BASED.equals(loadingMethod)) {
			pluginLoader = new ConfigBasedPluginLoader(pluginPath, configName);
		}
		else {
			pluginLoader = new AnnotationBasedPluginLoader(pluginPath);
		}
		
		eventListeners = pluginLoader.getAllRegisteredEventListeners();
	}

	/**
	 * Whenever an event is fired it has to be managed by this method.
	 * Therefore the plugin manager manages an event queue. This thread safe structure is filled with every event
	 * and the {@link EventQueueManager} is responsible for executing those events.
	 * When no {@link EventQueueManager} is instantiated the plugin manager instantiates one.
	 * 
	 * @param event
	 * 		the event which was fired and now has to be managed.
	 * */
	public void manageEvent(Event event) {
		try {
			eventQueue.put(event);
		} catch (InterruptedException e) {
			LOG.warn("InterruptedException while trying to put Event in queue: {}",
					e.getMessage());
		}

		if (eventQueueManager == null) {
			eventQueueManager = new EventQueueManager(eventQueue,
					eventListeners);
		}
		if (!eventQueueManager.isRunning()) {
			new Thread(eventQueueManager).start();
		}
	}
	
	/**
	 * This method shuts down the {@link EventQueueManager}. By doing so all pending events get
	 * discarded. Only use this method if that is the wanted behaviour or if the shutdown is safe
	 * (check using {@link #isSafeShutdown()}).
	 * */
	public void shutdown() {
		eventQueueManager.stop();
	}
	
	/**
	 * When there are no events in the queue left it is safe to shutdown. Otherwise it is not.
	 * 
	 * @return
	 * 		<code>true</code> if it is safe to shut down or <code>false</code> otherwise.
	 * */
	public boolean isSafeShutdown() {
		if(eventQueue.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * {@link org.simplug.framework.core.SimPlug#logLoadedPlugins()}
	 * */
	@Deprecated
	public void logPlugins() {
		LOG.info("== All known Event Listeners / Plugins:");
		for (String event : eventListeners.keySet()) {
			LOG.info("Listeners for event: {}", event);
			List<Class<?>> listeners = eventListeners.get(event);
			int i = 1;
			for (Class<?> listener : listeners) {
				LOG.info("{}. {}", i, listener.getName());
				i++;
			}
		}
	}
}
