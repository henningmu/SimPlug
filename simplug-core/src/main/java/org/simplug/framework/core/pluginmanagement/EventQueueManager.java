package org.simplug.framework.core.pluginmanagement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.simplug.framework.core.pluginmanagement.EventManager.NoListenersForEventException;
import org.simplug.framework.model.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventQueueManager implements Runnable {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventQueueManager.class);

	private LinkedHashMap<String, List<Class<?>>> eventListeners;
	private LinkedBlockingQueue<Event> eventQueue;
	private boolean running;

	public EventQueueManager(LinkedBlockingQueue<Event> eventQueue,
			LinkedHashMap<String, List<Class<?>>> eventListeners) {
		this.eventQueue = eventQueue;
		this.eventListeners = eventListeners;
	}

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

	public void stop() {
		LOG.info("Stopped with a queue of {}. All Events are discarded.", eventQueue.size());
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
}
