package de.olfillasodikno.rvgl.server.events;

import de.olfillasodikno.rvgl.server.structures.AbstractEvent;
import de.olfillasodikno.rvgl.server.structures.Lobby;
import de.olfillasodikno.rvgl.server.structures.Player;

public class PlayerJoinLobbyEvent extends AbstractEvent{
	
	private Player player;
	
	private Lobby lobby;
	
	public PlayerJoinLobbyEvent(Player player, Lobby lobby) {
		this.player = player;
		this.lobby = lobby;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
	
	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
}
