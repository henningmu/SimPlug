package org.simplug.framework.model;

import org.simplug.framework.model.events.DestroyEvent;
import org.simplug.framework.model.events.Event;
import org.simplug.framework.model.events.InitEvent;
import org.simplug.framework.model.events.InterruptEvent;

/**
 * Core Plugin class. Every Plugin has to extend this class in order to
 * work properly. This class defines the lifecycle of a plugin and 
 * provides some methods made available through the @see SimPlugContext.
 * The lifecycle of a plugin is defined as follows:
 * onInit -> onEventReceived -> onInterrupt -> onDestroy [TODO: pic of lifecycle]
 * */
public class Plugin {

	private SimPlugContext context;
	
	/**
	 * This is the core method from a plugin to receive events from
	 * the framework. It receives every event and delegates it to the
	 * corresponding lifecycle methods of the plugin.
	 * An @see InitEvent is delegated to the @see Plugin.onInit() method.
	 * An @see InterruptEvent is delegated to the @see Plugin.onInterrupt() method.
	 * A @see DestroyEvent is delegated to the @see Plugin.onDestroy() method.
	 * Every other event received is delegated to the @see Plugin.onEventReceived() method.
	 * This method does no validity check on the passed event it just passes it on.
	 * 
	 * @param event
	 * 		the event to receive and delegate to the plugins methods
	 * */
	public final void receiveAndDelegateEvent(Event event) {
		if (event instanceof InitEvent) {
			onInit((InitEvent) event);
		} else if (event instanceof InterruptEvent) {
			onInterrupt((InterruptEvent) event);
		} else if (event instanceof DestroyEvent) {
			onDestroy((DestroyEvent) event);
		} else {
			onEventReceived(event);
		}
	}
	
	/**
	 * This methods uses the fireEvent method provided from the @see SimPlugContext in order
	 * to fire events on it own. @see SimPlugContext.fireEvent(Event event).
	 * 
	 * @param event
	 * 		the Event to be fired
	 * */
	public final void fireEvent(Event event) {
		this.context.fireEvent(event);
	}
	
	/**
	 * This is the first method in the lifecycle of a plugin.
	 * It extracts the SimPlugContext from the event to make it
	 * accessible for possible later operations.
	 * 
	 * @param event
	 * 		the init event used to initialize the plugin
	 * */
	public void onInit(InitEvent event) {
		InitEvent initEvent = (InitEvent) event;
		
		this.context = initEvent.getContext();
	}
	
	/**
	 * This method gets called when no other lifecycle method reacts on an event.
	 * It is responsible for handling every event the plugin registered to.
	 * 
	 * @param event
	 * 		the received event to react on
	 * */
	public void onEventReceived(Event event) {
	}

	/**
	 * This method reacts on @see InterruptEvent. An InterruptEvent should be fired to
	 * stop any action in the plugin. The stop is not urgent and the plugin still has time
	 * to save all collected data or similar. Although it is possible the plugin should 
	 * not continue to work on a task when it gets an InterruptEvent. It is likely to
	 * receive a DestroyEvent right after the InterruptEvent.
	 * 
	 * @param event
	 * 		the received interrupt event
	 * */
	public void onInterrupt(InterruptEvent event) {
	}

	/**
	 * This method reacts on @see DestroyEvent. A DestroyEvent immediatly stops the execution
	 * of a plugin. No saving of states is possible.
	 * 
	 * @param event
	 * 		the received destroy event
	 * */
	public void onDestroy(DestroyEvent event) {
	}
	
	/**
	 * return the context binded to this plugin or null if no context is bound
	 * */
	public SimPlugContext getContext() {
		return this.context;
	}
	
	/**
	 * Use this method to bind the plugin to another context other than supplied
	 * with the initial InitEvent.
	 * 
	 * @param context
	 * 		the context to bind to this plugin
	 * */
	public void setContext(SimPlugContext context) {
		this.context = context;
	}
}
