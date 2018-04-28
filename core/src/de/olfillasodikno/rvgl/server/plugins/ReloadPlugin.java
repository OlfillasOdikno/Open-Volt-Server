package de.olfillasodikno.rvgl.server.plugins;

import de.olfillasodikno.rvgl.server.events.ServerCommandEvent;
import de.olfillasodikno.rvgl.server.structures.Event;
import de.olfillasodikno.rvgl.server.structures.Plugin;

public class ReloadPlugin extends Plugin{
	
	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
	
	@Event
	public void onServerCommand(ServerCommandEvent ev) {
		String line = ev.getLine();
		String[] split = line.split(" ",2);
		if(!split[0].equalsIgnoreCase("rl")) {
			return;
		}
		if(split.length!=2) {
			error("rl needs just a boolean");
			return;
		}
		boolean reloadConfig = Boolean.parseBoolean(split[1]);
		getServer().getPluginManager().reloadAll(reloadConfig);
	}

}
