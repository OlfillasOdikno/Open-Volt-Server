package de.olfillasodikno.rvgl.server.events;

import de.olfillasodikno.rvgl.server.structures.AbstractEvent;
import de.olfillasodikno.rvgl.server.structures.Player;

public class PlayerJoinEvent extends AbstractEvent{
	
	private Player player;
	
	public PlayerJoinEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
}
