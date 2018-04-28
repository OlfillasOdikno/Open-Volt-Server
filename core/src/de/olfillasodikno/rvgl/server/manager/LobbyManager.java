package de.olfillasodikno.rvgl.server.manager;

import java.util.HashMap;

import de.olfillasodikno.rvgl.server.Server;
import de.olfillasodikno.rvgl.server.structures.GameSettings;
import de.olfillasodikno.rvgl.server.structures.Lobby;
import de.olfillasodikno.rvgl.server.utils.StringUtils;

public class LobbyManager {

	private Lobby hub;
	
	private HashMap<String,Lobby> lobbies;
	private final Server server;
	public LobbyManager(Server server) {
		lobbies = new HashMap<>();
		this.server = server;
		hub = new Lobby(server);
		GameSettings hubSettings = hub.getSettings();
		hubSettings.setNum_cars((byte) 127);
	}
		
	public Lobby getHub() {
		return hub;
	}
	
	public Lobby newLobby() {
		String code;
		int max = 10;
		while(lobbies.containsKey((code = StringUtils.random(6)))){
			max--;
			if(max == 0) {
				break;
			}
		}
		if(code == null) {
			return null;
		}
		Lobby lobby = new Lobby(server);
		lobby.setCode(code);
		lobbies.put(code, lobby);
		return lobby;
	}
	
	public Lobby getLobby(String code) {
		return lobbies.get(code);
	}
	
	public HashMap<String, Lobby> getLobbies() {
		return lobbies;
	}
}
