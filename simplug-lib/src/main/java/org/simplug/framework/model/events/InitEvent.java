package org.simplug.framework.model.events;

import org.simplug.framework.model.SimPlugContext;

/**
 * Most basic initilization event. It only adds functionality to manage
 * the {@link org.simplug.framework.model.SimPlugContext} which has to be provided with every
 * instance of this class or its subclasses.
 * */
public class InitEvent extends Event {
	private SimPlugContext context;
	
	/**
	 * Constructor setting the context the plugin should be bound to later.
	 * @param context
	 * 		the context the plugin should be bound to.
	 * */
	public InitEvent(SimPlugContext context) {
		this.context = context;
	}
	
	/**
	 * Returns the context set via the constructor.
	 * @return
	 * 		the context set via the constructor.
	 * */
	public SimPlugContext getContext() {
		return context;
	}
}
