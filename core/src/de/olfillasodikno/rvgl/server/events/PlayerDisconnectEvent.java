package de.olfillasodikno.rvgl.server.events;

import de.olfillasodikno.rvgl.server.structures.AbstractEvent;
import de.olfillasodikno.rvgl.server.structures.Player;

public class PlayerDisconnectEvent extends AbstractEvent{
	
	private Player player;
	
	public PlayerDisconnectEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
}
