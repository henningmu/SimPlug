package org.simplug.framework.model.events;

/**
 * Abstract Event class which can be used for implementing more specific events. 
 * It holds all the basic information every event needs to work properly.
 * */
public abstract class Event {
	private Class<?> eventReceiver;
	private String descriptor;

	/**
	 * Simple constructor which initializes the event by setting the event descriptor.
	 * */
	public Event() {
		this.descriptor = this.getClass().getName();
	}

	/**
	 * This method returns the package + class name of the event as default descriptor.<br />
	 * When you need another descriptor for an event override this method
	 * and return any value / constant you want. Make sure the class always returns the
	 * same so no inconsistencies come up.
	 * 
	 * @return
	 * 		the descriptor for this event.
	 * */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * Manually sets the receiver for this event.<br />
	 * The use of this method is <b>STRONGLY discouraged</b> as it undermines the event based system.
	 * 
	 * @param eventReceiver
	 * 		the receiver of this event.
	 * */
	public void setEventReceiver(Class<?> eventReceiver) {
		this.eventReceiver = eventReceiver;
	}
	
	/**
	 * Returns the receiver of this event or <code>null</code> if none has been set manually.
	 * @return
	 * 		the receiver of this event or <code>null</code> if none has been set manually.
	 * */
	public Class<?> getEventReceiver() {
		return eventReceiver;
	}
	
	/**
	 * The toString method is overridden to return the event descriptor.
	 * It returns the same as {@link #getDescriptor()}.<br />
	 * If you want to supply a custom descriptor for an event you do not have to override this method again.
	 * 
	 * @return the descriptor of this event
	 * */
	@Override
	public String toString() {
		return this.getDescriptor();
	}
}
