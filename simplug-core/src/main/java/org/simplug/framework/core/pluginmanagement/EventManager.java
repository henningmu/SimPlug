package org.simplug.framework.core.pluginmanagement;

import java.util.List;

import org.simplug.framework.core.SimPlug;
import org.simplug.framework.model.Plugin;
import org.simplug.framework.model.events.Event;
import org.simplug.framework.model.events.InitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventManager implements Runnable {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventManager.class);

	private Event event;
	private List<Class<?>> eventListeners;

	public EventManager(Event event, List<Class<?>> eventListeners)
			throws NoListenersForEventException {
		if (eventListeners == null || eventListeners.size() == 0) {
			throw new NoListenersForEventException("No Listeners for Event "
					+ event.getDescriptor());
		}

		this.event = event;
		this.eventListeners = eventListeners;
	}

	public void run() {
		for (Class<?> clazz : eventListeners) {
			try {
				Plugin plugin = (Plugin) clazz.newInstance();

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

	public class NoListenersForEventException extends Exception {
		private static final long serialVersionUID = -7214237814298549505L;

		public NoListenersForEventException(String message) {
			super(message);
		}
	}
}
