package org.simplug.framework.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.simplug.framework.core.config.Configuration;
import org.simplug.framework.core.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoaderUtilities {

	private static final Logger LOG = LoggerFactory
			.getLogger(ClassLoaderUtilities.class);

	public static LinkedHashMap<String, List<Class<?>>> getEventListeners(File[] pluginJarFiles) {
		
		LinkedHashMap<String, List<Class<?>>> eventListeners = new LinkedHashMap<String, List<Class<?>>>();
		URLClassLoader classLoader = new URLClassLoader(
				JarUtilities.convertFilesToUrls(pluginJarFiles));
		ConfigurationManager configurationManager = ConfigurationManager
				.getInstance();

		for (File pluginJar : pluginJarFiles) {
			try {
				FileInputStream fileStreamJar = new FileInputStream(pluginJar);
				JarInputStream jarInputStream = new JarInputStream(
						fileStreamJar);

				// Search for config
				ZipEntry entry;
				while ((entry = jarInputStream.getNextEntry()) != null) {
					boolean isPluginConf = entry.getName().toLowerCase()
							.endsWith(configurationManager.getProperty(Configuration.CONF_PLUGIN_CONFIG_NAME));
					if (isPluginConf) {
						ZipFile jarFile = new ZipFile(pluginJar);
						InputStream configInputStream = jarFile
								.getInputStream(entry);
						Properties configProperties = new Properties();
						configProperties.load(configInputStream);
						configInputStream.close();
						eventListeners = addEventListeners(configProperties,
								classLoader, eventListeners);
						break;
					}

				}
				jarInputStream.close();
				fileStreamJar.close();
			} catch (FileNotFoundException e) {
				LOG.warn("FileNotFoundException while trying to open a JarInputStream. "
						+ "Responsible file: " + pluginJar.getName());
				return null;
			} catch (IOException e) {
				LOG.warn("IOException while trying to open a JarInputStream. "
						+ " Responsible file: " + pluginJar.getName());
				return null;
			}
		}
		
		return eventListeners;
	}

	private static LinkedHashMap<String, List<Class<?>>> addEventListeners(
			Properties pluginConfiguration, URLClassLoader classLoader,
			LinkedHashMap<String, List<Class<?>>> currentEventListeners) {

		for (String keyEvent : pluginConfiguration.stringPropertyNames()) {
			List<Class<?>> currentListeners = currentEventListeners
					.get(keyEvent);
			Class<?> listeningClass = null;
			String className = pluginConfiguration.getProperty(keyEvent);
			if (isPluginClass(className, classLoader) == false) {
				LOG.info("Class " + className
						+ " is not a valid plugin. It is registered to listen for event " + keyEvent
						+ " but does not extend the Plugin class provided in simplug-model.");
			}
			try {
				listeningClass = classLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
				LOG.warn("ClassNotFoundException while reading plugin configuration. Tried to create class: "
						+ pluginConfiguration.getProperty(keyEvent)
						+ " to listen for event: " + keyEvent);
			}

			if (currentListeners == null) {
				currentListeners = new ArrayList<Class<?>>();
			}
			currentListeners.add(listeningClass);
			currentEventListeners.put(keyEvent, currentListeners);
		}

		return currentEventListeners;
	}

	private static boolean isPluginClass(String className,
			URLClassLoader classLoader) {
//		try {
//			Class<?> clazz = classLoader.loadClass(className);
//			if (clazz.getSuperclass().equals(Plugin.class)) {
//				return true;
//			}
//		} catch (ClassNotFoundException e) {
//			LOG.warn("ClassNotFoundException while trying to determine whether class is a plugin or not. "
//					+ "Faulty class name: " + className);
//			return false;
//		}
//
//		return false;
		return true;
	}
}
