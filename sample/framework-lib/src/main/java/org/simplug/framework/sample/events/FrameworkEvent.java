package org.simplug.framework.sample.events;

import org.simplug.framework.model.events.Event;

public abstract class FrameworkEvent extends Event {
	public String dataString;

	public FrameworkEvent(String dataString) {
		this.dataString = dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	public String getDataString() {
		return dataString;
	}
}
