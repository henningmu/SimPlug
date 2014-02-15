package org.simplug.framework.model.events;

/**
 * Abstract Event class used for implementing more specific events. TODO: Make
 * this class serializable / parceable
 * */
public abstract class Event {
	private Class<?> eventReceiver;
	private String descriptor;

	public Event() {
		this.descriptor = this.getClass().getName();
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setEventReceiver(Class<?> eventReceiver) {
		this.eventReceiver = eventReceiver;
	}
	
	public Class<?> getEventReceiver() {
		return eventReceiver;
	}
	
	@Override
	public String toString() {
		return descriptor;
	}
}
