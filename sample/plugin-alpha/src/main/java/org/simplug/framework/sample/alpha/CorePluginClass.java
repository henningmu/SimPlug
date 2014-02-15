package org.simplug.framework.sample.alpha;

import org.simplug.framework.model.Plugin;
import org.simplug.framework.model.annotations.ListenTo;
import org.simplug.framework.model.events.Event;
import org.simplug.framework.sample.events.AlphaEvent;
import org.simplug.framework.sample.events.BetaEvent;
import org.simplug.framework.sample.events.GammaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ListenTo( {AlphaEvent.class, GammaEvent.class} )
public class CorePluginClass extends Plugin {

	private static final Logger LOG = LoggerFactory
			.getLogger(CorePluginClass.class);

	@Override
	public void onEventReceived(Event event) {
		if (event instanceof GammaEvent) {
			GammaEvent gammaEvent = (GammaEvent) event;
			LOG.info("Alpha Plugin received a gamma event with following data: {}",
					gammaEvent.getDataString());
			super.fireEvent(new BetaEvent("Event coming from alpha"));
		} else if (event instanceof AlphaEvent) {
			AlphaEvent alphaEvent = (AlphaEvent) event;
			LOG.info("Alpha Plugin received an alpha event with following data: {}",
					alphaEvent.getDataString());
		}
	}
}
