package de.olfillasodikno.rvgl.server.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import de.olfillasodikno.rvgl.server.Server;
import de.olfillasodikno.rvgl.server.structures.AbstractEvent;
import de.olfillasodikno.rvgl.server.structures.Event;

public class EventHandler {

	public final HashMap<Class<? extends AbstractEvent>, CopyOnWriteArrayList<Listener>> listenermap;
	private final HashMap<Object, ArrayList<Listener>> eventmap;
	
	private final Server server;

	public EventHandler(Server server) {
		this.server = server;
		this.listenermap = new HashMap<>();
		this.eventmap = new HashMap<>();
	}

	public void onEvent(AbstractEvent event) {
		CopyOnWriteArrayList<Listener> listeners = listenermap.get(event.getClass());
		if(listeners == null) {
			return;
		}
		for (Listener listener : listeners) {
			listener.invoke(event);
		}
	}

	public void unregisterListener(Object o) {
		ArrayList<Listener> listeners = eventmap.remove(o);
		if (listeners == null) {
			server.error("Listener not registered!");
			return;
		}
		for (Listener listener : listeners) {
			listenermap.get(listener.ev_class).remove(listener);
		}
	}

	public void registerListener(Object o) {
		for (Method method : o.getClass().getMethods()) {
			Event ev = method.getAnnotation(Event.class);
			if (ev == null || method.getParameterTypes().length != 1) {
				continue;
			}
			Class<?> parameter = method.getParameterTypes()[0];
			if (parameter.getSuperclass() != AbstractEvent.class) {
				continue;
			}
			Class<? extends AbstractEvent> ev_class = parameter.asSubclass(AbstractEvent.class);
			if (!listenermap.containsKey(parameter)) {
				listenermap.put(ev_class, new CopyOnWriteArrayList<>());
			}
			Listener listener = new Listener(o, method, ev_class);
			listenermap.get(ev_class).add(listener);
			if (!eventmap.containsKey(o)) {
				eventmap.put(o, new ArrayList<>());
			}
			eventmap.get(o).add(listener);
		}
	}
	
	public Server getServer() {
		return server;
	}

	private static class Listener {
		private Object instance;
		private Method method;
		private Class<? extends AbstractEvent> ev_class;

		public Listener(Object instance, Method method, Class<? extends AbstractEvent> ev_class) {
			this.instance = instance;
			this.method = method;
			this.ev_class = ev_class;
		}

		public void invoke(AbstractEvent ev) {
			try {
				method.setAccessible(true);
				method.invoke(instance, ev);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
