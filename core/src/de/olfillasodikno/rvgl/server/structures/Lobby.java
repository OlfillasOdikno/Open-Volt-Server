package de.olfillasodikno.rvgl.server.structures;

import java.util.ArrayList;

import de.olfillasodikno.rvgl.server.Server;
import de.olfillasodikno.rvgl.server.events.PlayerJoinLobbyEvent;
import de.olfillasodikno.rvgl.server.events.PlayerLeaveLobbyEvent;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameSettings;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerData;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerDisconnect;
import de.olfillasodikno.rvgl.server.network.packets.PacketRequestPlayerId;

public class Lobby {

	private ArrayList<Player> players;

	private Player owner;

	private final GameSettings settings;

	private final Game game;

	private String joinCode;

	private boolean privateLobby;

	private final Server server;

	public Lobby(Server server) {
		players = new ArrayList<>();
		settings = new GameSettings();
		this.server = server;
		game = new Game(settings, this);
	}

	public Server getServer() {
		return server;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Player getOwner() {
		return owner;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		addPlayer(player, false);
	}

	public void addPlayer(Player player, boolean silent) {
		PlayerJoinLobbyEvent ev = new PlayerJoinLobbyEvent(player, this);
		ev.fire();
		if ((!players.contains(player)) && !ev.isCanceled()) {
			PacketRequestPlayerId joinPkt = new PacketRequestPlayerId();
			joinPkt.setId(player.getId());
			broadcast(joinPkt);

			// PacketPlayerAdddress addr = new PacketPlayerAdddress(player);
			// broadcast(addr);

			if (player.getData() != null && player.getData().getCarData() != null) {
				PacketPlayerData dataPkt = new PacketPlayerData(player);
				broadcast(dataPkt);
			}
			if (player.getCurrentLobby() != null) {
				player.getCurrentLobby().removePlayer(player);
			}

			for (Player p : players) {
				PacketRequestPlayerId outPacket = new PacketRequestPlayerId();
				outPacket.setId(p.getId());

				// PacketPlayerAdddress addrOut = new PacketPlayerAdddress(p);

				player.sendPacket(outPacket, false);
				// player.sendPacket(addrOut, false);
				if (p.getData() != null && p.getData().getCarData() != null) {
					PacketPlayerData otherData = new PacketPlayerData(p);
					player.sendPacket(otherData, false);
				}
			}

			players.add(player);
			player.setCurrentLobby(this);

			PacketGameSettings pkt = new PacketGameSettings(settings);
			player.sendPacket(pkt);
			if (!silent) {
				player.sendMsg("Lobby joined successfully.");
			}
		}
	}

	public GameSettings getSettings() {
		return settings;
	}

	public void removePlayer(Player player) {
		players.remove(player);
		players.forEach(p -> {
			PacketPlayerDisconnect disPkt = new PacketPlayerDisconnect(p.getId());
			player.sendPacket(disPkt);
		});
		PacketPlayerDisconnect disPkt = new PacketPlayerDisconnect(player.getId());
		broadcast(disPkt);
		PlayerLeaveLobbyEvent ev = new PlayerLeaveLobbyEvent(player, this);
		ev.fire();
	}

	public boolean isPrivateLobby() {
		return privateLobby;
	}

	public void setPrivateLobby(boolean privateLobby) {
		this.privateLobby = privateLobby;
	}

	public void setCode(String code) {
		this.joinCode = code;
	}

	public String getJoinCode() {
		return joinCode;
	}

	public Game getGame() {
		return game;
	}

	public void broadcast(Packet pkt) {
		broadcast(pkt, null, false);
	}

	public void broadcast(Packet pkt, Player sender, boolean exclude) {
		for (Player p : players) {
			if (exclude && p == sender) {
				continue;
			}
			p.sendPacket(pkt, false);
		}
	}

	public boolean isFull() {
		return players.size() >= settings.getNum_cars();
	}
}
