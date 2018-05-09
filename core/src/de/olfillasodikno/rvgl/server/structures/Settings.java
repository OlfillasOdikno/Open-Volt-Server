package de.olfillasodikno.rvgl.server.structures;

import de.olfillasodikno.rvgl.server.Constants;

public class Settings implements Hashable{
	private int hash = hash();

	private short port = Constants.DEFAULT_PORT;
	private int maxPlayers = Constants.DEFAULT_MAX_PLAYERS;
	private int refreshRate = Constants.DEFAULT_REFERSH_RATE;
	
	private String pluginDir = "plugins";
	
	@Override
	public int hash(){
		return Hashable.hash(Settings.class);
	}

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public short getPort() {
		return port;
	}

	public void setPort(short port) {
		this.port = port;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getPluginDir() {
		return pluginDir;
	}

	public void setPluginDir(String pluginDir) {
		this.pluginDir = pluginDir;
	}
	
	public int getRefreshRate() {
		return refreshRate;
	}
	
	public void setRefreshRate(int refreshRate) {
		this.refreshRate = refreshRate;
	}
}
