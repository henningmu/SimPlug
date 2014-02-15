package org.simplug.framework.core.util;

import java.util.LinkedHashMap;
import java.util.List;

public interface PluginLoader {
	
	public LinkedHashMap<String, List<Class<?>>> getAllRegisteredEventListeners();
}
