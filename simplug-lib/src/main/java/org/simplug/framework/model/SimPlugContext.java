package org.simplug.framework.model;

import org.simplug.framework.model.events.Event;

public interface SimPlugContext {
	public void fireEvent(Event event);
}
