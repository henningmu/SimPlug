package org.simplug.framework.model;

import org.simplug.framework.model.events.Event;

/**
 * Interface defining the core functionality of the SimPlug Framework which has
 * to be available to all plugins to work properly. The Context is later passed
 * to the plugins when initializing them to give them access to all necessary
 * functions.
 * */
public interface SimPlugContext {
	/**
	 * This method takes an event and fires it to all registered listeners. It
	 * does not return anything. If you expect an answer from an event create an
	 * response event and register for it.
	 * 
	 * @parm event The event to be fired
	 * */
	public void fireEvent(Event event);
}
