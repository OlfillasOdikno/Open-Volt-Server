package de.olfillasodikno.rvgl.server.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.olfillasodikno.rvgl.server.Constants;
import de.olfillasodikno.rvgl.server.Server;
import de.olfillasodikno.rvgl.server.structures.Command;
import de.olfillasodikno.rvgl.server.structures.Command.CommandInfo;
import de.olfillasodikno.rvgl.server.structures.Player;
import de.olfillasodikno.rvgl.server.structures.Plugin;
import de.olfillasodikno.rvgl.server.structures.PluginConfig;

public class PluginManager {
	private static final Logger logger = Logger.getLogger(PluginManager.class.getName());

	private final HashMap<String, Command> cmds;

	private final HashMap<Class<? extends Plugin>, Plugin> instancesMap;

	private final Server server;

	public PluginManager(Server server) {
		this.cmds = new HashMap<>();
		this.instancesMap = new HashMap<>();
		this.server = server;
	}

	public String parseHandleCmd(String cmd, Player player) {
		cmd = cmd.trim();
		if (cmd.length() < 2) {
			return null;
		}
		if (cmd.startsWith(Constants.COMMAND_PREFIX)) {
			cmd = cmd.substring(1);
		}
		String[] split = cmd.split(" ", 2);
		if (split.length == 1) {
			return handleCommand(split[0], null, player);
		}
		return handleCommand(split[0], split[1], player);
	}

	public String handleCommand(String cmd, String args, Player player) {
		if (!cmds.containsKey(cmd)) {
			return null;
		}
		Command command = cmds.get(cmd);
		if (!player.hasPermission(command.getInfo().permission())) {
			return "You don't have the permission to execute this command!";
		}
		return command.fire(args, player);
	}

	public Plugin unregisterPlugin(Class<? extends Plugin> plugin) {
		Plugin instance = instancesMap.remove(plugin);
		if (instance != null) {
			unregisterPlugin(instance);
		}
		return instance;
	}

	public void unregisterPlugin(Plugin instance) {
		instance.onDisable();
		instance.log("Disabled");
		List<Command> commands = instance.getCommands();
		for (Command cmd : commands) {
			cmds.remove(cmd.getInfo().name());
		}
		server.getEventHandler().unregisterListener(instance);
	}

	public Plugin registerPlugin(Class<? extends Plugin> plugin) throws InstantiationException, IllegalAccessException {
		return registerPlugin(plugin, null);
	}

	public void reload(Class<? extends Plugin> plugin, boolean reloadConfig) {
		Plugin instance = unregisterPlugin(plugin);
		PluginConfig config = null;
		if (instance != null) {
			config = instance.getPluginConfig();
		}
		if (reloadConfig && config != null) {
			config = server.getFileManager().reloadConfig(config);
		}
		try {
			registerPlugin(plugin, config);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e.getCause());
		}
	}

	public void reloadAll(boolean reloadConfig) {
		Collection<Plugin> instances = instancesMap.values();
		ArrayList<Plugin> old = new ArrayList<>();
		for (Plugin instance : instances) {
			unregisterPlugin(instance);
			old.add(instance);
		}
		instancesMap.clear();
		for (Plugin instance : old) {
			try {
				PluginConfig config = instance.getPluginConfig();
				if (reloadConfig && config != null) {
					config = server.getFileManager().reloadConfig(config);
				}
				registerPlugin(instance.getClass(), config);
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e.getCause());
			}
		}
		old.clear();
	}

	public Plugin registerPlugin(Class<? extends Plugin> plugin, PluginConfig config)
			throws InstantiationException, IllegalAccessException {
		Plugin instance = plugin.newInstance();
		if (config != null) {
			instance.setPluginConfig(config);
		}
		instance.setServer(server);
		ArrayList<Command> commands = new ArrayList<>();
		for (Method m : plugin.getMethods()) {
			if (m.isAnnotationPresent(CommandInfo.class)) {
				commands.add(registerCommand(m, instance));
			}
		}
		instance.setCommands(commands);
		instancesMap.put(plugin, instance);
		server.getEventHandler().registerListener(instance);
		instance.onEnable();
		instance.log("Enabled");
		return instance;
	}

	private Command registerCommand(Method m, Plugin module) {
		CommandInfo info = m.getAnnotation(CommandInfo.class);
		Command cmd = new Command(m, module, info);
		cmds.put(cmd.getInfo().name(), cmd);
		return cmd;
	}

}
