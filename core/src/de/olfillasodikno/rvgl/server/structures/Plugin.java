package de.olfillasodikno.rvgl.server.structures;

import java.util.ArrayList;

import de.olfillasodikno.rvgl.server.Server;

public abstract class Plugin {

	private Server server;

	private ArrayList<Command> commands;

	private PluginConfig config;

	public Plugin() {
		this.config = PluginConfig.generateDefault(this);
	}

	public abstract void onEnable();

	public abstract void onDisable();

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public PluginConfig getPluginConfig() {
		return config;
	}

	public void setPluginConfig(PluginConfig config) {
		this.config = config;
	}

	public void setCommands(ArrayList<Command> commands) {
		this.commands = commands;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	public void log(String msg) {
		System.out.println("[" + config.name + "] " + msg);
	}

	public void error(String msg) {
		System.err.println("[" + config.name + "] " + msg);
	}

}
