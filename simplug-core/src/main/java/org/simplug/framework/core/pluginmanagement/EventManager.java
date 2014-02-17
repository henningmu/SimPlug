package org.simplug.framework.core.pluginmanagement;

import java.util.List;

import org.simplug.framework.core.SimPlug;
import org.simplug.framework.model.Plugin;
import org.simplug.framework.model.events.Event;
import org.simplug.framework.model.events.InitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages a single Event and its delegation to the listening classes.
 * It is implementated as Runnable and started in a new Thread to support
 * multithreading and the parallel execution of multiple events.<br />
 * At the moment there are no strategies which listener receives the event.
 * All registered listeners receive the event.
 * */
public class EventManager implements Runnable {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventManager.class);

	private Event event;
	private List<Class<?>> eventListeners;

	/**
	 * Constructor responsible for constructing an EventManager. When there are
	 * no registered listeners for the passed event it throws a {@link NoListenersForEventException}.
	 * 
	 * @param event
	 * 		the event which should be managed.
	 * @param eventListeners
	 * 		all registered listeners for the specified element.
	 * @throws NoListenersForEventException
	 * 		when the eventListeners parameter is null or empty
	 * */
	public EventManager(Event event, List<Class<?>> eventListeners)
			throws NoListenersForEventException {
		if (eventListeners == null || eventListeners.size() == 0) {
			throw new NoListenersForEventException("No Listeners for Event "
					+ event.getDescriptor());
		}

		this.event = event;
		this.eventListeners = eventListeners;
	}

	/**
	 * 
	 * */
	public void run() {
		for (Class<?> clazz : eventListeners) {
			try {
				Plugin plugin = null;
				if(event.getEventReceiver() != null) {
					plugin = (Plugin) event.getEventReceiver().newInstance();
				}
				else {
					plugin = (Plugin) clazz.newInstance();
				}
				
				// TODO: Determine if plugin is already started ...
				InitEvent initEvent = new InitEvent(SimPlug.getInstance());

				plugin.receiveAndDelegateEvent(initEvent);
				plugin.receiveAndDelegateEvent(event);
			} catch (ClassCastException cce) {
				LOG.warn("ClassCastException while trying to cast class () to Plugin: {}",
						new Object[] { clazz.getName(), cce.getMessage() });
			} catch (InstantiationException ie) {
				LOG.warn("InstantiationException while trying to cast class () to Plugin: {}",
						new Object[] { clazz.getName(), ie.getMessage() });
			} catch (IllegalAccessException iae) {
				LOG.warn("IllegalAccesstException while trying to cast class () to Plugin: {}",
						new Object[] { clazz.getName(), iae.getMessage() });
			}
		}
	}

	/**
	 * Exception which can be thrown when no listeners were found for a specific event.
	 * */
	public class NoListenersForEventException extends Exception {
		private static final long serialVersionUID = -7214237814298549505L;

		/**
		 * Simple constructor only calling the super constructor.
		 * */
		public NoListenersForEventException(String message) {
			super(message);
		}
	}
}
