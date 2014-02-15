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

import org.simplug.framework.model.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigBasedPluginLoader implements PluginLoader {

	private static final Logger LOG = LoggerFactory
			.getLogger(ConfigBasedPluginLoader.class);

	private String pluginPath;
	private String configName;

	public ConfigBasedPluginLoader(String pluginPath, String configName) {
		this.pluginPath = pluginPath;
		this.configName = configName;
	}

	public LinkedHashMap<String, List<Class<?>>> getAllRegisteredEventListeners() {
		LinkedHashMap<String, List<Class<?>>> registeredEventListeners = new LinkedHashMap<String, List<Class<?>>>();

		File[] pluginJarFiles = JarFileUtilities.getAllJarFilesFromPath(pluginPath);
		URLClassLoader classLoader = new URLClassLoader(
				JarFileUtilities.convertFilesToUrls(pluginJarFiles));

		for (File pluginJar : pluginJarFiles) {
			Properties configProperties = registerAllListenerFromPlugin(pluginJar,
					classLoader);
			
			registeredEventListeners = addEventListeners(configProperties,
					classLoader, registeredEventListeners);
		}

		return registeredEventListeners;
	}

	private Properties registerAllListenerFromPlugin(
			File pluginJar, URLClassLoader classLoader) {
		Properties configProperties = new Properties();
		try {
			FileInputStream fileStreamJar = new FileInputStream(pluginJar);
			JarInputStream jarInputStream = new JarInputStream(fileStreamJar);

			ZipEntry entry;
			while ((entry = jarInputStream.getNextEntry()) != null) {
				boolean isPluginConf = entry.getName().toLowerCase().endsWith(configName);
				
				if (isPluginConf) {
					ZipFile jarFile = new ZipFile(pluginJar);
					InputStream configInputStream = jarFile.getInputStream(entry);
				
					configProperties.load(configInputStream);
					
					configInputStream.close();
					break;
				}
			}
			jarInputStream.close();
			fileStreamJar.close();
		} catch (FileNotFoundException e) {
			LOG.warn("FileNotFoundException while trying to open a JarInputStream. "
					+ "Responsible file: {}", pluginJar.getName());
			return null;
		} catch (IOException e) {
			LOG.warn("IOException while trying to open a JarInputStream. "
					+ " Responsible file: {}", pluginJar.getName());
			return null;
		}

		return configProperties;
	}

	private LinkedHashMap<String, List<Class<?>>> addEventListeners(
			Properties pluginConfiguration, URLClassLoader classLoader,
			LinkedHashMap<String, List<Class<?>>> currentEventListeners) {

		for (String keyEvent : pluginConfiguration.stringPropertyNames()) {
			List<Class<?>> currentListeners = currentEventListeners
					.get(keyEvent);
			Class<?> listeningClass = null;
			String className = pluginConfiguration.getProperty(keyEvent);
			if (isPluginClass(className, classLoader) == false) {
				LOG.info("Class {} is not a valid plugin. It is registered to listen for event {}"
						+ " but does not extend the Plugin class provided in simplug-model.",
						new Object[]{ className, keyEvent });
			}
			try {
				listeningClass = classLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
				LOG.warn("ClassNotFoundException while reading plugin configuration. Tried to create class: {}"
						+ " to listen for event: {}", new Object[]{ pluginConfiguration.getProperty(keyEvent), keyEvent });
			}

			if (currentListeners == null) {
				currentListeners = new ArrayList<Class<?>>();
			}
			currentListeners.add(listeningClass);
			currentEventListeners.put(keyEvent, currentListeners);
		}

		return currentEventListeners;
	}

	private boolean isPluginClass(String className, URLClassLoader classLoader) {
		 try {
			 Class<?> clazz = classLoader.loadClass(className);
			 if (clazz.getSuperclass().equals(Plugin.class)) {
				 return true;
			 }
		 } catch (ClassNotFoundException e) {
			 LOG.warn("ClassNotFoundException while trying to determine whether class is a plugin or not. "
					 + "Faulty class name: {}", className);
			 return false;
		 }
		 return false;
	}

}
