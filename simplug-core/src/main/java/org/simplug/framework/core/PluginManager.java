package org.simplug.framework.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import org.simplug.framework.core.config.Configuration;
import org.simplug.framework.core.config.ConfigurationManager;
import org.simplug.framework.core.util.ClassLoaderUtilities;
import org.simplug.framework.core.util.JarUtilities;
import org.simplug.framework.model.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(PluginManager.class);

	private LinkedHashMap<String, List<Class<?>>> eventListeners;

	public PluginManager() {

	}

	public void loadAndClassifyPlugins() {
		ConfigurationManager configurationManager = ConfigurationManager
				.getInstance();

		File[] jarFiles = JarUtilities
				.getAllJarFilesFromPath(configurationManager
						.getProperty(Configuration.CONF_PLUGIN_DIRECTORY));

		eventListeners = ClassLoaderUtilities.getEventListeners(jarFiles);
	}

	public void delegateEvent(String event, Object data) {
		List<Class<?>> listeningClasses = eventListeners.get(event);
		for (Class<?> clazz : listeningClasses) {
			try {
				Plugin pluginImpl = (Plugin) clazz.newInstance();
				pluginImpl.start();
			} catch (InstantiationException e) {
				LOG.warn(e.getMessage());
			} catch (IllegalAccessException e) {
				LOG.warn(e.getMessage());
			}
		}
	}

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
