package de.olfillasodikno.rvgl.server.structures;

import de.olfillasodikno.rvgl.server.Constants;

public class Settings implements Hashable{
	public int hash = hash();

	public short port = Constants.DEFAULT_PORT;
	public int maxPlayers = Constants.DEFAULT_MAX_PLAYERS;
	
	public String pluginDir = "plugins";
	
	@Override
	public int hash(){
		return Hashable.hash(Settings.class);
	}
}
