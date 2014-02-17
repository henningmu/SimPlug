package org.simplug.framework.core.util;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Simple interface to define what a plugin loader has to be capable of doing.
 * */
public interface PluginLoader {
	/**
	 * This method returns a mapping for all registered listeners on an event.<br />
	 * Any implementation of this interface has to be able to get all registered event listeners
	 * and save them inside a LinkedHashMap with the event descriptor as key and a list of all
	 * classes which are listening to that event as value.
	 * 
	 * @return a structure representing all registered event listeners. The keys of this structure
	 * 		are the event descriptors and the values are all listening classes to that event.
	 * */
	public LinkedHashMap<String, List<Class<?>>> getAllRegisteredEventListeners();
}
