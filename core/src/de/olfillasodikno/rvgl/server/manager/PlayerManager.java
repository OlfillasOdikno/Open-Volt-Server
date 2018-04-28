package de.olfillasodikno.rvgl.server.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.olfillasodikno.libenet.Peer;
import de.olfillasodikno.rvgl.server.Server;
import de.olfillasodikno.rvgl.server.structures.Player;

public class PlayerManager {

	private int maxPlayers;

	private final HashMap<Integer, Player> idMap;
	private final HashMap<Long, Player> peerMap;

	private Stack<Integer> unused;

	private final Server server;

	public PlayerManager(int maxPlayers, Server server) {
		idMap = new HashMap<>();
		peerMap = new HashMap<>();
		this.server = server;
		this.maxPlayers = maxPlayers;
		unused = new Stack<>();
		for (int i = 0; i < maxPlayers; i++) {
			unused.push(i);
		}
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	private int getNextId() {
		if (unused.empty()) {
			return -1;
		}
		return unused.pop();
	}

	public Player getPlayer(Peer peer) {
		if (!peerMap.containsKey(peer.getHandle())) {
			Player player = newPlayer();
			player.setPeer(peer);
			peerMap.put(peer.getHandle(), player);
		}
		Player player = peerMap.get(peer.getHandle());
		return player;
	}

	public Player getPlayer(long peerHandle) {
		if (!peerMap.containsKey(peerHandle)) {
			return null;
		}
		Player player = peerMap.get(peerHandle);
		return player;
	}

	public void removePlayer(Player p) {
		idMap.remove(p.getId());
		peerMap.remove(p.getPeer().getHandle());
		unused.push(p.getId());
	}

	private Player newPlayer() {
		int id = getNextId();
		Player player = new Player(id, server);
		idMap.put(id, player);
		return player;
	}

	public Set<Map.Entry<Integer, Player>> getPlayers() {
		return idMap.entrySet();
	}

	public HashMap<Integer, Player> getPlayerMap() {
		return idMap;
	}

	public HashMap<Long, Player> getPeerMap() {
		return peerMap;
	}
}
