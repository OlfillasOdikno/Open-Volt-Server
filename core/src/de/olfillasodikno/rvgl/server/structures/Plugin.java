package de.olfillasodikno.rvgl.server.structures;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.olfillasodikno.rvgl.server.Server;

public abstract class Plugin {
	
	private static final Logger logger = Logger.getLogger(Plugin.class.getName());

	private Server server;

	private List<Command> commands;

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

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	public List<Command> getCommands() {
		return commands;
	}

	public void log(String msg) {
		logger.log(Level.INFO,"[" + config.getName() + "] {0}", msg);
	}

	public void error(String msg) {
		logger.log(Level.INFO,"[" + config.getName() + "] {0}", msg);
	}

}
