package de.olfillasodikno.autocountdown;

import de.olfillasodikno.rvgl.server.events.PostPacketEvent;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameCountdown;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameLoaded;
import de.olfillasodikno.rvgl.server.structures.Event;
import de.olfillasodikno.rvgl.server.structures.Game;
import de.olfillasodikno.rvgl.server.structures.Game.GamePlayerData;
import de.olfillasodikno.rvgl.server.structures.Lobby;
import de.olfillasodikno.rvgl.server.structures.Plugin;

public class AutoCountdown extends Plugin {

	private int time;

	@Override
	public void onEnable() {
		time = getPluginConfig().getExternalConfig().getAsJsonObject().get("time").getAsInt();
	}

	@Override
	public void onDisable() {

	}

	@Event
	public void onPacket(PostPacketEvent ev) {
		if (ev.getPacket() instanceof PacketGameLoaded) {
			Lobby lobby = ev.getSender().getCurrentLobby();
			Game game = lobby.getGame();
			boolean fail = false;
			for (GamePlayerData data : game.getPlayerData().values()) {
				if (!data.isLoaded()) {
					fail = true;
					break;
				}
			}

			if (fail) {
				return;
			}

			PacketGameCountdown countPkt = new PacketGameCountdown(getServer().getLastNS() + time * 1_000_000_000,
					1_000_000_000L);
			lobby.broadcast(countPkt);
			game.setInRace(true);
		}
	}

}
