package org.simplug.framework.sample.framework;

import org.simplug.framework.core.SimPlug;

public class Main {
	public static void main(String[] args) {
		SimPlug simPlug = SimPlug.getInstance();
		simPlug.loadPlugins();
		simPlug.logLoadedPlugins();
	}
}
