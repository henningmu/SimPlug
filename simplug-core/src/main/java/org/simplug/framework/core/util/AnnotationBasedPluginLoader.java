package org.simplug.framework.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.simplug.framework.model.annotations.ListenTo;
import org.simplug.framework.model.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the @see PluginLoader interface.
 * This class represents a plugin loader using annotations to load all
 * classes listening to events from a specified directory. It loads all classes
 * annotated with the @see ListenTo annotation
 * */
public class AnnotationBasedPluginLoader implements PluginLoader {

	private static final Logger LOG = LoggerFactory
			.getLogger(AnnotationBasedPluginLoader.class);

	private String pluginPath;
	private LinkedHashMap<String, List<Class<?>>> registeredEventListeners;

	/**
	 * Constructs an annotation based plugin loader. It scans the given path for all
	 * plugins and scans every class for the @see ListenTo annotation.
	 * 
	 * @param pluginPath
	 * 		the directory in which the plugins are stored
	 * */	
	public AnnotationBasedPluginLoader(String pluginPath) {
		this.pluginPath = pluginPath;
		registeredEventListeners = new LinkedHashMap<String, List<Class<?>>>();
	}

	/**
	 * This method returns all registered event listeners extracted from all plugins.
	 * It loads all plugins from the specified path and scans every class for the @see
	 * ListenTo annotation. Every class is then stored in the structure which is returned later.
	 * 
	 * @return
	 * 		the structure holding all events as key and a list of listening classes as corresponding value
	 * */
	public LinkedHashMap<String, List<Class<?>>> getAllRegisteredEventListeners() {
		File[] pluginJarFiles = JarFileUtilities
				.getAllJarFilesFromPath(pluginPath);
		URLClassLoader classLoader = new URLClassLoader(
				JarFileUtilities.convertFilesToUrls(pluginJarFiles));

		for (File pluginJar : pluginJarFiles) {
			registerAllListenerFromPlugin(pluginJar, classLoader);
		}
		
		return registeredEventListeners;
	}

	private void registerAllListenerFromPlugin(File pluginJar,
			URLClassLoader classLoader) {
		try {
			JarInputStream jarInputStream = new JarInputStream(
					new FileInputStream(pluginJar));

			JarEntry entry = null;
			while ((entry = jarInputStream.getNextJarEntry()) != null) {
				boolean isClass = entry.getName().toLowerCase()
						.endsWith(".class");
				if (isClass) {
					// stripe the .class extension and replace / by . to get
					// the actual package description
					String actualClassName = entry.getName()
							.substring(0, entry.getName().length() - 6)
							.replace("/", ".");
					
					Class<?> clazz = classLoader.loadClass(actualClassName);
					if (clazz.isAnnotationPresent(ListenTo.class)) {
						Class<? extends Event>[] events = clazz.getAnnotation(ListenTo.class).value();
						for (Class<? extends Event> eventClass : events) {
							String event = eventClass.getName();
							addEventListener(event, clazz);
						}
					}
				}
			}

			jarInputStream.close();
		} catch (FileNotFoundException fnfe) {
			LOG.warn(
					"FileNotFoundException while trying to access a jar ({}) to search for annotations: {}",
					pluginJar.getName(), fnfe.getMessage());
		} catch (IOException ioe) {
			LOG.warn(
					"IOException while trying to access a jar ({}) to search for annotations: {}",
					pluginJar.getName(), ioe.getMessage());
		} catch (ClassNotFoundException cnfe) {
			LOG.warn(
					"ClassNotFoundException while trying to load a class from the jar ({}): {}",
					pluginJar.getName(), cnfe.getMessage());

		}
	}

	private void addEventListener(String event, Class<?> listener) {
		List<Class<?>> listeners = registeredEventListeners.get(event);

		if (listeners == null) {
			listeners = new ArrayList<Class<?>>();
		}

		listeners.add(listener);
		registeredEventListeners.put(event, listeners);
	}
}
