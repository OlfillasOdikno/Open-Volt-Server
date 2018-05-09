package de.olfillasodikno.rvgl.server.structures;

import java.util.ArrayList;
import java.util.List;

import de.olfillasodikno.libenet.Peer;
import de.olfillasodikno.rvgl.server.Server;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerChat;

public class Player {

	private final int id;
	private Data data;
	private Peer peer;
	private Server server;

	private Lobby currentLobby;

	private final List<String> permissions;

	public Player(int id, Server server) {
		this.server = server;
		this.id = id;
		data = new Data();
		permissions = new ArrayList<>();
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public boolean hasPermission(String permission) {
		for(String perm : permissions) {
			if(permission.matches(perm)) {
				return true;
			}
		}
		return false;
	}

	public void addPermission(String permission) {
		permissions.add(permission);
	}

	public int getId() {
		return id;
	}

	public Data getData() {
		return data;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public Peer getPeer() {
		return peer;
	}

	public void sendPacket(Packet pkt) {
		server.sendPacket(getPeer(), pkt);
	}

	public Lobby getCurrentLobby() {
		return currentLobby;
	}

	public void setCurrentLobby(Lobby currentLobby) {
		this.currentLobby = currentLobby;
	}

	public void sendMsg(String ret) {
		ArrayList<String> lines = new ArrayList<>();
		for (String s : ret.split("\n")) {
			while (!s.equals("")) {
				String line = s.substring(0, Math.min(s.length(), 59)).trim();
				if (line.length() < 60) {
					lines.add(line);
					break;
				}
				int lastWhite = 0;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == ' ') {
						lastWhite = i;
					}
				}
				if (lastWhite == 0) {
					lastWhite = Math.min(line.length() - 1, 59);
				}
				line = line.substring(0, lastWhite).trim();
				if (line.isEmpty()) {
					break;
				}
				lines.add(line);
				if (s.length() > lastWhite) {
					s = s.substring(lastWhite + 1).trim();
				}
			}
		}
		for (String line : lines) {
			PacketPlayerChat pkt = new PacketPlayerChat(0, line);
			sendPacket(pkt);
		}
	}

	public static final class Data {
		private byte[] playername = new byte[16];
		
		private String playerNameString;

		private boolean cheat;

		private boolean ready;
		private boolean spectator;

		private CarData carData;
		
		public Data() {
			this.carData = new CarData();
		}

		public byte[] getPlayername() {
			return playername;
		}
		
		public String getPlayernameString() {
			return playerNameString;
		}

		public void setPlayername(byte[] playername) {
			if (playername.length == this.playername.length) {
				this.playername = playername;
			} else {
				System.arraycopy(playername, 0, this.playername, 0,
						Math.min(playername.length, this.playername.length));
			}
			this.playerNameString = new String(this.playername).replaceAll("\0", "");
		}
		
		public CarData getCarData() {
			return carData;
		}

		public boolean isCheat() {
			return cheat;
		}

		public void setCheat(boolean cheat) {
			this.cheat = cheat;
		}

		public boolean isReady() {
			return ready;
		}

		public boolean isSpectator() {
			return spectator;
		}

		public void setReady(boolean ready) {
			this.ready = ready;
		}

		public void setSpectator(boolean spectator) {
			this.spectator = spectator;
		}

	}
}
