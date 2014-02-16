package org.simplug.framework.model;

import org.simplug.framework.model.events.DestroyEvent;
import org.simplug.framework.model.events.Event;
import org.simplug.framework.model.events.InitEvent;
import org.simplug.framework.model.events.InterruptEvent;

/**
 * Core Plugin class. Every Plugin has to extend this class in order to work properly. <br />
 * This class defines the lifecycle of a plugin and provides some methods made available
 * through the {@link SimPlugContext} interface. The lifecycle of a plugin is defined as follows:<br />
 * <img src="doc-files/Plugin-1.png" />
 * */
public class Plugin {

	private SimPlugContext context;
	
	/**
	 * This is the core method of a plugin to receive events from
	 * the framework. It receives every event and delegates it to the
	 * corresponding lifecycle methods of the plugin.<br />
	 * This method is final because there is no need to overwrite in extending plugin classes. The
	 * defined plugin lifecycle can not be changed by any plugin.<br />
	 * It delegates the received events using the following scheme:<br />
	 * An {@link InitEvent}is delegated to the {@link #onInit(InitEvent)} method.<br />
	 * An {@link InterruptEvent} is delegated to the {@link #onInterrupt(InterruptEvent)} method.<br />
	 * A {@link DestroyEvent} is delegated to the {@link #onDestroy(DestroyEvent)} method.<br />
	 * Every other event received is delegated to the {@link #onEventReceived(Event)} method.<br /><br />
	 * This method does no validity check on the passed event. It just passes it on.
	 * 
	 * @param event
	 * 		the event to receive and delegate to the plugins lifecycle methods.
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
	 * This methods uses the method implemented by the {@link SimPlugContext#fireEvent(Event)}
	 * interface in order to fire events on it own.
	 * @see SimPlugContext#fireEvent(Event)
	 * 
	 * @param event
	 * 		the {@link Event} to be fired.
	 * */
	public final void fireEvent(Event event) {
		this.context.fireEvent(event);
	}
	
	/**
	 * This lifecycle method reacts on any {@link InitEvent}.<br />
	 * It extracts the SimPlugContext from the event to make it
	 * accessible for possible later operations.
	 * 
	 * @param event
	 * 		the {@link InitEvent} to be used to initialize the plugin.
	 * */
	public void onInit(InitEvent event) {
		InitEvent initEvent = (InitEvent) event;
		
		this.context = initEvent.getContext();
	}
	
	/**
	 * This method gets called when no other lifecycle method reacts on an event.<br />
	 * It is responsible for handling every event the plugin registered to.
	 * 
	 * @param event
	 * 		the received {@link Event}.
	 * */
	public void onEventReceived(Event event) {
	}

	/**
	 * This lifecycle method reacts on any {@link InterruptEvent}.<br />
	 * An InterruptEvent should be fired to stop any action in the plugin. The stop is not urgent
	 * and the plugin still has time to save all collected data or similar.
	 * Although it is possible the plugin should not continue to work on a task when it gets an {@link InterruptEvent}.
	 * It is likely to receive a {@link DestroyEvent} right after the {@link InterruptEvent}.
	 * 
	 * @param event
	 * 		the received {@link InterruptEvent}.
	 * */
	public void onInterrupt(InterruptEvent event) {
	}

	/**
	 * This lifecycle method reacts on any {@link DestroyEvent}.<br />
	 * A DestroyEvent immediatly stops the execution of a plugin. No saving of states is possible.
	 * 
	 * @param event
	 * 		the received {@link DestroyEvent}.
	 * */
	public void onDestroy(DestroyEvent event) {
	}
	
	/**
	 * Returns the context bound to this plugin or <code>null</code> if no context is bound.
	 * @return
	 * 		the context bound to this plugin or <code>null</code> if no context is bound.
	 * */
	public SimPlugContext getContext() {
		return this.context;
	}
	
	/**
	 * Use this method to bind the plugin to another context different from the one 
	 * supplied with the initial {@link InitEvent}.
	 * 
	 * @param context
	 * 		the context this plugin should be bound to.
	 * */
	public void setContext(SimPlugContext context) {
		this.context = context;
	}
}
