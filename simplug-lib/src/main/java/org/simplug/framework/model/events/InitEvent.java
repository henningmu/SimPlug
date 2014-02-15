package org.simplug.framework.model.events;

import org.simplug.framework.model.SimPlugContext;

public class InitEvent extends Event {
	private SimPlugContext context;
	
	public InitEvent(SimPlugContext context) {
		this.context = context;
	}
	
	public SimPlugContext getContext() {
		return context;
	}
}
