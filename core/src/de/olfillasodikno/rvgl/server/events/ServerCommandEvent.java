package de.olfillasodikno.rvgl.server.events;

import de.olfillasodikno.rvgl.server.structures.AbstractEvent;

public class ServerCommandEvent extends AbstractEvent{

	private String line;
	
	public ServerCommandEvent(String line) {
		this.line = line;
	}
	
	public String getLine() {
		return line;
	}
	
}
