package de.olfillasodikno.rvgl.server.structures;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import de.olfillasodikno.libenet.Enet;
import de.olfillasodikno.rvgl.server.network.packets.PacketIngameData;
import de.olfillasodikno.rvgl.server.structures.ingame.IngameObject;
import de.olfillasodikno.rvgl.server.structures.ingame.IngamePlayer;

public class Game {

	private int restarts;

	private int numPlayers;

	private HashMap<Player, GamePlayerData> playerData;

	private int lastCarID;

	private final GameSettings settings;

	private boolean running;
	private boolean inRace;

	private Lobby lobby;

	private Player hostPlayer;
	private IngamePlayer ingamePlayer;

	public Game(GameSettings settings, Lobby lobby) {
		this.lobby = lobby;
		playerData = new HashMap<>();
		this.settings = settings;
		restarts = 0;
		lastCarID = 0;
		inRace = false;
	}

	public void addPlayer(Player player) {
		GamePlayerData data = new GamePlayerData(player);
		data.setCarID(lastCarID);
		playerData.put(player, data);
		lastCarID++;
		numPlayers++;
	}

	public byte[] getTrackname() {
		return settings.getTrackname();
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public int getRestarts() {
		return restarts;
	}

	public HashMap<Player, GamePlayerData> getPlayerData() {
		return playerData;
	}

	public void update() {
		numPlayers = 0;
		restarts = 0;
		lastCarID = 0;
		inRace = false;
		running = false;
		playerData.clear();
	}
	
	public IngamePlayer getIngamePlayer() {
		return ingamePlayer;
	}
	
	public Player getHostPlayer() {
		return hostPlayer;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public void setInRace(boolean inRace) {
		this.inRace = inRace;
	}

	public boolean isInRace() {
		return inRace;
	}

	public void sendData(Player p) {
//		PacketIngameData pkt = new PacketIngameData(objects);
//
//		byte[] out = lobby.getServer().getHandler().encode(pkt);
//		long h = Enet.enet_packet_create(out, out.length, 0);
//		Enet.enet_peer_send(p.getPeer().getHandle(), (char) 1, h);
	}

	public void updateGameObjects(PacketIngameData pkt, Player sender) {
		ArrayList<IngameObject> objects = pkt.getObjects();

		PacketIngameData pkt2 = new PacketIngameData(objects);
		
		for(Player p : sender.getCurrentLobby().getPlayers()) {
			byte[] out = lobby.getServer().getHandler().encode(pkt2);
			long h = Enet.enet_packet_create(out, out.length, 0);
			Enet.enet_peer_send(p.getPeer().getHandle(), (char) 1, h);
		}
		
//		Iterator<IngameObject> it = objects.iterator();
//		while (it.hasNext()) {
//			IngameObject object = it.next();
//			if (object instanceof IngamePlayer) {
//				IngamePlayer player = (IngamePlayer) object;
//				if (player.getId() != sender.getId()) {
//					it.remove();
//				}else {
//					playerData.get(sender).setLast(player);
//				}
//			} else {
//				if(!(object instanceof IngameHorn)) {
//					it.remove();					
//				}
//			}
//		}
//		for (IngameObject obj : this.objects) {
//			if (obj instanceof IngamePlayer) {
//				IngamePlayer player = (IngamePlayer) obj;
//				if (player.getId() == sender.getId()) {
//					continue;
//				}
//			}
//			if(!(obj instanceof IngameHorn)) {
//				objects.add(obj);				
//			}
//		}
//		this.objects = objects;
	}

	public static final class GamePlayerData {

		private boolean loaded;
		private boolean finished;
		
		private Player player;

		private int carID;
		
		private IngamePlayer last;
		
		public void setLast(IngamePlayer last) {
			this.last = last;
		}
		
		public IngamePlayer getLast() {
			return last;
		}

		public GamePlayerData(Player p) {
			this.player = p;
		}

		public void encode(ByteBuffer buf) {
			buf.putInt(player.getId());
			buf.putInt(carID);
			buf.putInt(0); // UNKNOWN
			buf.putInt(0); // UNKNOWN
			buf.putInt(0); // UNKNOWN
			buf.putInt(0); // UNKNOWN
			buf.putInt(player.getId());
			buf.putInt(0); // UNKNOWN
			buf.put(player.getData().getCarData().getCarname(), 0, 20);
			buf.put(player.getData().getPlayername(), 0, 16);
		}
		
		public boolean isLoaded() {
			return loaded;
		}
		
		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}

		public void setCarID(int carID) {
			this.carID = carID;
		}

		public int getCarID() {
			return carID;
		}

		public boolean isFinished() {
			return finished;
		}

		public void setFinished(boolean finished) {
			this.finished = finished;
		}

		
	}
}
