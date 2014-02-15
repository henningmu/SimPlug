package org.simplug.framework.core.pluginmanagement;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.simplug.framework.core.config.Configuration;
import org.simplug.framework.core.config.ConfigurationManager;
import org.simplug.framework.core.util.ClassLoaderUtilities;
import org.simplug.framework.core.util.JarUtilities;
import org.simplug.framework.model.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(PluginManager.class);

	private LinkedHashMap<String, List<Class<?>>> eventListeners;
	private LinkedBlockingQueue<Event> eventQueue;
	private EventQueueManager eventQueueManager;

	public PluginManager() {
		eventQueue = new LinkedBlockingQueue<Event>();
	}

	public void loadAndClassifyPlugins() {
		ConfigurationManager configurationManager = ConfigurationManager
				.getInstance();

		File[] jarFiles = JarUtilities
				.getAllJarFilesFromPath(configurationManager
						.getProperty(Configuration.CONF_PLUGIN_DIRECTORY));

		eventListeners = ClassLoaderUtilities.getEventListeners(jarFiles);
	}

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
	
	public void shutdown() {
		eventQueueManager.stop();
	}
	
	public boolean isSafeShutdown() {
		if(eventQueue.size() == 0) {
			return true;
		}
		return false;
	}

	@Deprecated
	public void logPlugins() {
		LOG.info("== All known Event Listeners / Plugins:");
		for (String event : eventListeners.keySet()) {
			LOG.info("Listeners for event: " + event);
			List<Class<?>> listeners = eventListeners.get(event);
			int i = 1;
			for (Class<?> listener : listeners) {
				LOG.info("" + i + ". " + listener.getName());
				i++;
			}
		}
	}
}
