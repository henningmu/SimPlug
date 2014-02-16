package org.simplug.framework.model.events;

import org.simplug.framework.model.SimPlugContext;

/**
 * Most basic Init Event. It only adds functionality to manage
 * the SimPlugContext which has to be provided with every InitEvent.
 * */
public class InitEvent extends Event {
	private SimPlugContext context;
	
	/**
	 * @param context
	 * 		the context the plugin should be bound to.
	 * */
	public InitEvent(SimPlugContext context) {
		this.context = context;
	}
	
	/**
	 * @return the context set via the constructor.
	 * */
	public SimPlugContext getContext() {
		return context;
	}
}
