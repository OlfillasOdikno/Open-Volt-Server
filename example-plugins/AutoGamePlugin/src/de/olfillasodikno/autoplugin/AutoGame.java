package de.olfillasodikno.autoplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.olfillasodikno.rvgl.server.events.PlayerFinishEvent;
import de.olfillasodikno.rvgl.server.events.PlayerLeaveLobbyEvent;
import de.olfillasodikno.rvgl.server.events.PlayerReadyEvent;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameStart;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerChat;
import de.olfillasodikno.rvgl.server.structures.Event;
import de.olfillasodikno.rvgl.server.structures.Game;
import de.olfillasodikno.rvgl.server.structures.Game.GamePlayerData;
import de.olfillasodikno.rvgl.server.structures.Lobby;
import de.olfillasodikno.rvgl.server.structures.Player;
import de.olfillasodikno.rvgl.server.structures.Plugin;

public class AutoGame extends Plugin {

	private String[] tracknames;

	private Lobby current;
	private int remaining;

	private Random rnd;

	private Runnable lobbyTimer;

	private HashMap<Lobby, Integer> lrestart;

	private int timer_lobby;
	private int timer_first;
	private int timer_half;
	private int timer_last;
	private int timer_all;

	private int min_players;
	private int max_players;

	private int laps;
	private int difficulty;

	private boolean pickups;

	@Override
	public void onEnable() {
		lrestart = new HashMap<>();
		rnd = new Random();
		tracknames = getPluginConfig().getGson()
				.fromJson(getPluginConfig().getExternalConfig().getAsJsonObject().get("tracks"), String[].class);

		timer_lobby = getPluginConfig().getExternalConfig().getAsJsonObject().get("timer_lobby").getAsInt();

		timer_first = getPluginConfig().getExternalConfig().getAsJsonObject().get("timer_first").getAsInt();
		timer_half = getPluginConfig().getExternalConfig().getAsJsonObject().get("timer_half").getAsInt();
		timer_last = getPluginConfig().getExternalConfig().getAsJsonObject().get("timer_last").getAsInt();
		timer_all = getPluginConfig().getExternalConfig().getAsJsonObject().get("timer_all").getAsInt();

		min_players = getPluginConfig().getExternalConfig().getAsJsonObject().get("min_players").getAsInt();
		max_players = getPluginConfig().getExternalConfig().getAsJsonObject().get("max_players").getAsInt();

		laps = getPluginConfig().getExternalConfig().getAsJsonObject().get("laps").getAsInt();
		difficulty = getPluginConfig().getExternalConfig().getAsJsonObject().get("difficulty").getAsInt();

		pickups = getPluginConfig().getExternalConfig().getAsJsonObject().get("pickups").getAsBoolean();

		newLobby();
		lobbyTimer = new LobbyTimer(this);
		Thread lobbyThread = new Thread(lobbyTimer);
		lobbyThread.start();
	}

	@Override
	public void onDisable() {

	}

	@Event
	public void onFinisch(PlayerFinishEvent ev) {
		Lobby lobby = ev.getPlayer().getCurrentLobby();
		updateFinish(lobby);
	}

	public void updateFinish(Lobby lobby) {
		Game game = lobby.getGame();

		int finished = 0;
		for (GamePlayerData data : game.getPlayerData().values()) {
			if (data.isFinished()) {
				finished++;
			}
		}
		if (finished == lobby.getPlayers().size()) {
			lrestart.put(lobby, timer_all);
		} else if (finished == lobby.getPlayers().size() - 1) {
			lrestart.put(lobby, timer_last);
		} else if (finished >= lobby.getPlayers().size() / 2) {
			lrestart.put(lobby, timer_half);
		} else if (finished == 1) {
			lrestart.put(lobby, timer_first);
		}
	}

	@Event
	public void onReady(PlayerReadyEvent ev) {
		if (ev.getPlayer().getCurrentLobby() == getServer().getLobbyManager().getHub()) {
			joinNextFreeLobby(ev.getPlayer());
		}
	}

	@Event
	public void onPlayerLeave(PlayerLeaveLobbyEvent ev) {
		updateFinish(ev.getLobby());
	}

	public void joinNextFreeLobby(Player p) {
		current.addPlayer(p);
		if (current.isFull()) {
			start();
		}
	}

	private void start() {
		Game game = current.getGame();
		for (Player p : current.getPlayers()) {
			game.addPlayer(p);
		}
		game.setRunning(true);
		PacketGameStart startPkt = new PacketGameStart(game);
		current.broadcast(startPkt);
		newLobby();
	}

	private void newLobby() {
		current = new Lobby(getServer());
		current.getSettings().setNumCars((byte) max_players);
		current.setPrivateLobby(true);
		String track = tracknames[rnd.nextInt(tracknames.length)];
		current.getSettings().setTrackname(track.getBytes());
		current.getSettings().setLimit((byte) laps);
		current.getSettings().setDifficulty((byte) difficulty);
		current.getSettings().setPickups(pickups);
	}

	private void updateTime() {
		ArrayList<Lobby> remove = new ArrayList<>();
		for (Map.Entry<Lobby, Integer> entry : lrestart.entrySet()) {
			Lobby lobby = entry.getKey();
			Game game = lobby.getGame();
			if (entry.getValue() <= 5 || entry.getValue() == 30 || entry.getValue() == 10) {
				boolean unfinished = false;
				for (GamePlayerData data : game.getPlayerData().values()) {
					if (!data.isFinished()) {
						unfinished = true;
						break;
					}
				}
				if ((entry.getValue() == 30 || entry.getValue() == 10) && unfinished) {
					PacketPlayerChat pkt = new PacketPlayerChat(0, "Waiting for players to finish..");
					entry.getKey().broadcast(pkt);
				}
				if (!unfinished || (entry.getValue() == 30 || entry.getValue() == 10 || entry.getValue() <= 3)) {
					PacketPlayerChat pkt = new PacketPlayerChat(0, "Time until restart: " + entry.getValue() + "s ");
					entry.getKey().broadcast(pkt);
				}
			}
			entry.setValue(entry.getValue() - 1);
			if (entry.getValue() <= 0) {
				String track = tracknames[rnd.nextInt(tracknames.length)];
				game.getSettings().setTrackname(track.getBytes());
				game.update();
				for (Player p : lobby.getPlayers()) {
					game.addPlayer(p);
				}
				game.setRunning(true);
				PacketGameStart startPkt = new PacketGameStart(game);
				lobby.broadcast(startPkt);
				remove.add(lobby);
			}
		}
		for (Lobby l : remove) {
			lrestart.remove(l);
		}
		if (current.getPlayers().size() >= min_players) {
			if (remaining < 10 || remaining % 10 == 0) {
				PacketPlayerChat pkt = new PacketPlayerChat(0, "Time until start: " + remaining);
				current.broadcast(pkt);
			}
			if (remaining <= 0) {
				start();
			}
			remaining--;
		} else {
			remaining = timer_lobby;
		}
	}

	private class LobbyTimer implements Runnable {

		private AutoGame lobby;

		public LobbyTimer(AutoGame lobby) {
			this.lobby = lobby;
		}

		@Override
		public void run() {
			while (!lobby.getServer().isDown()) {

				lobby.updateTime();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
