package de.olfillasodikno.rvgl.server.plugins;

import de.olfillasodikno.rvgl.server.events.ServerCommandEvent;
import de.olfillasodikno.rvgl.server.structures.Event;
import de.olfillasodikno.rvgl.server.structures.Player;
import de.olfillasodikno.rvgl.server.structures.Plugin;

public class OpPlugin extends Plugin{
	
	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}
	
	@Event
	public void onServerCommand(ServerCommandEvent ev) {
		String line = ev.getLine();
		String[] split = line.split(" ",2);
		if(!split[0].equalsIgnoreCase("op")) {
			return;
		}
		if(split.length!=2) {
			error("op needs just the playername as argument..");
			return;
		}
		String playername = split[1];
		for(Player player : getServer().getPlayerManager().getPlayerMap().values()) {
			if(playername.equals(player.getData().getPlayernameString())){
				player.addPermission(".*");
				player.sendMsg("You are now op!");
				log("Player "+playername+" is now op!");
				return;
			}
		}
		error("Player "+playername+" not found...");
	}

}
