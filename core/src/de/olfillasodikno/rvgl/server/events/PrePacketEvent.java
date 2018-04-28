package de.olfillasodikno.rvgl.server.events;

import de.olfillasodikno.rvgl.server.structures.AbstractEvent;
import de.olfillasodikno.rvgl.server.structures.Packet;
import de.olfillasodikno.rvgl.server.structures.Player;

public class PrePacketEvent extends AbstractEvent{

	private Packet packet;
	
	private Player sender;
		
	public PrePacketEvent(Packet packet, Player sender) {
		this.packet = packet;
		this.sender = sender;
	}
	
	public Packet getPacket() {
		return packet;
	}

	public Player getSender() {
		return sender;
	}
	
}
