package org.simplug.framework.sample.framework;

import org.simplug.framework.core.SimPlug;
import org.simplug.framework.sample.events.AlphaEvent;
import org.simplug.framework.sample.events.BetaEvent;
import org.simplug.framework.sample.events.GammaEvent;

public class Main {
	public static void main(String[] args) {
		SimPlug simPlug = SimPlug.getInstance();
		simPlug.loadPlugins();
		simPlug.logLoadedPlugins();
		
		simPlug.fireEvent(AlphaEvent.class.getName(), null);
		simPlug.fireEvent(BetaEvent.class.getName(), null);
		simPlug.fireEvent(GammaEvent.class.getName(), null);
		simPlug.fireEvent(AlphaEvent.class.getName(), null);
		simPlug.fireEvent(BetaEvent.class.getName(), null);
	}
}
