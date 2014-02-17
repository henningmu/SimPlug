package org.simplug.framework.core.pluginmanagement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.simplug.framework.core.pluginmanagement.EventManager.NoListenersForEventException;
import org.simplug.framework.model.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages the queue of events.<br />
 * It is implemented as a runnable running in a new Thread. It listens on a
 * {@link LinkedBlockingQueue} for new events. When there is a new event it takes it
 * process it in a new Thread using {@link EventManager}.
 * */
public class EventQueueManager implements Runnable {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventQueueManager.class);

	private LinkedHashMap<String, List<Class<?>>> eventListeners;
	private LinkedBlockingQueue<Event> eventQueue;
	private boolean running;

	/**
	 * Constructor responsible for creating an EventQueueManager.
	 * It only saves the managed members. It does not start monitoring the queue.
	 * 
	 * @param eventQueue
	 * 		the event queue to monitor for new events.
	 * @param eventListeners
	 * 		structure containing a mapping betwenn events and listeners.
	 * */
	public EventQueueManager(LinkedBlockingQueue<Event> eventQueue,
			LinkedHashMap<String, List<Class<?>>> eventListeners) {
		this.eventQueue = eventQueue;
		this.eventListeners = eventListeners;
	}

	/**
	 * 
	 * */
	public void run() {
		running = true;
		while (running) {
			try {
				Event event = eventQueue.take();
				if(event != null) {		
					EventManager eventManager = new EventManager(event, eventListeners.get(event.getDescriptor()));
					new Thread(eventManager).start();
				}
			} catch (InterruptedException e) {
				LOG.warn("InterruptedException in Event Queue Manager while trying to take an event from queue: {}",
						e.getMessage());
			} catch (NoListenersForEventException nlfee) {
				LOG.warn("NoListenersForEventException in Event Queue Manager: {}",
						nlfee.getMessage());
			}
		}
	}

	
	/**
	 * Stops the EventQueueManager immediately discarding all pending events from the queue.
	 * */
	public void stop() {
		LOG.info("Stopped with a queue of {}. All Events are discarded.", eventQueue.size());
		running = false;
	}

	/**
	 * Returns <code>true</code> if the EventQueueManager is running at the moment.
	 * If the EventQueueManager is stopped it returns <code>false</code>.
	 * 
	 * @return
	 * 		<code>true</code> if EventQueueManager is running otherwise returns <code>false</code>.
	 * */
	public boolean isRunning() {
		return running;
	}
}
