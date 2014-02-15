package org.simplug.framework.sample.beta;

import org.simplug.framework.model.Plugin;
import org.simplug.framework.model.events.Event;
import org.simplug.framework.sample.events.AlphaEvent;
import org.simplug.framework.sample.events.BetaEvent;
import org.simplug.framework.sample.events.GammaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorePluginClass extends Plugin {
	private static final Logger LOG = LoggerFactory
			.getLogger(CorePluginClass.class);

	@Override
	public void onEventReceived(Event event) {
		if (event instanceof GammaEvent) {
			GammaEvent gammaEvent = (GammaEvent) event;
			LOG.info("Beta Plugin received a gamma event with following data: {}",
					gammaEvent.getDataString());
		} else if (event instanceof BetaEvent) {
			BetaEvent betaEvent = (BetaEvent) event;
			LOG.info("Betaa Plugin received an beta event with following data: {}",
					betaEvent.getDataString());
			super.fireEvent(new AlphaEvent("Alpha event coming from beta"));
		}
	}
}
