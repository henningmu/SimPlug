package org.simplug.framework.model;

import org.simplug.framework.model.events.DestroyEvent;
import org.simplug.framework.model.events.Event;
import org.simplug.framework.model.events.InitEvent;
import org.simplug.framework.model.events.InterruptEvent;

public class Plugin {

	protected SimPlugContext context;
	
	public final void receiveAndDelegateEvent(Event event) {
		if (event instanceof InitEvent) {
			onInit(event);
		} else if (event instanceof InterruptEvent) {
			onInterrupt(event);
		} else if (event instanceof DestroyEvent) {
			onDestroy(event);
		} else {
			onEventReceived(event);
		}
	}
	
	public final void fireEvent(Event event) {
		this.context.fireEvent(event);
	}
	
	public void onInit(Event event) {
		InitEvent initEvent = (InitEvent) event;
		
		this.context = initEvent.getContext();
	}

	public void onEventReceived(Event event) {
	}

	public void onInterrupt(Event event) {
	}

	public void onDestroy(Event event) {
	}
}
