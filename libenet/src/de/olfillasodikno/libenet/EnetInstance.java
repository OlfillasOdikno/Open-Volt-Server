package de.olfillasodikno.libenet;

import java.util.ArrayList;

public class EnetInstance {
	
	private long hostHandle;
	
	private final ArrayList<Long> connections;
	
	public EnetInstance(long handle) {
		this.hostHandle = handle;
		connections = new ArrayList<>();
	}
	
	public long getHostHandle() {
		return hostHandle;
	}
	
	public void addConnection(long handle) {
		connections.add(handle);
	}
}
