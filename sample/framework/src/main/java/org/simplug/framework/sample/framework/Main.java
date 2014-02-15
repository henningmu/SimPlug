package org.simplug.framework.sample.framework;

import java.io.IOException;

import org.simplug.framework.core.SimPlug;
import org.simplug.framework.sample.events.AlphaEvent;
import org.simplug.framework.sample.events.BetaEvent;
import org.simplug.framework.sample.events.GammaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException, InterruptedException {
		SimPlug simPlug = SimPlug.getInstance();
		simPlug.loadPlugins();
		simPlug.logLoadedPlugins();
		
		simPlug.fireEvent(new AlphaEvent("first alpha event"));
		simPlug.fireEvent(new BetaEvent("first beta event"));
		simPlug.fireEvent(new GammaEvent("first gamma event"));
		
		while(!simPlug.isSafeShutdown()) {
			Thread.sleep(10);
		}
		
		simPlug.shutdown();
		
//		simPlug.fireEvent(AlphaEvent.class.getName(), null);
//		simPlug.fireEvent(BetaEvent.class.getName(), null);
//		simPlug.fireEvent(GammaEvent.class.getName(), null);
//		simPlug.fireEvent(AlphaEvent.class.getName(), null);
//		simPlug.fireEvent(BetaEvent.class.getName(), null);
	}
}
