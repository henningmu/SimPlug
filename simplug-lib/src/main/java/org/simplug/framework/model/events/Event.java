package org.simplug.framework.model.events;

/**
 * Abstract Event class used for implementing more specific events. TODO: Make
 * this class serializable / parceable
 * */
public abstract class Event {
	@SuppressWarnings("unused")
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
	
	@Override
	public String toString() {
		return descriptor;
	}
}
