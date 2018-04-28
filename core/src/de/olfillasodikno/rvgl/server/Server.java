package de.olfillasodikno.rvgl.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.olfillasodikno.libenet.Enet;
import de.olfillasodikno.libenet.EnetEvent;
import de.olfillasodikno.libenet.EnetInstance;
import de.olfillasodikno.libenet.Peer;
import de.olfillasodikno.rvgl.server.events.ServerCommandEvent;
import de.olfillasodikno.rvgl.server.manager.EventHandler;
import de.olfillasodikno.rvgl.server.manager.FileManager;
import de.olfillasodikno.rvgl.server.manager.LobbyManager;
import de.olfillasodikno.rvgl.server.manager.PlayerManager;
import de.olfillasodikno.rvgl.server.manager.PluginManager;
import de.olfillasodikno.rvgl.server.network.DataHandler;
import de.olfillasodikno.rvgl.server.network.PacketHandler;
import de.olfillasodikno.rvgl.server.plugins.OpPlugin;
import de.olfillasodikno.rvgl.server.plugins.ReloadPlugin;
import de.olfillasodikno.rvgl.server.structures.AbstractEvent;
import de.olfillasodikno.rvgl.server.structures.Packet;
import de.olfillasodikno.rvgl.server.structures.Settings;

public class Server implements Runnable {

	private long lastMS;

	private DataHandler<EnetEvent, byte[], Packet> handler;

	private boolean running;
	private boolean down;

	private short port;

	private long startMs;

	private EnetInstance enet_server;

	private PlayerManager playerManager;
	private EventHandler eventHandler;
	private PluginManager pluginManager;
	private LobbyManager lobbyManager;

	private Settings settings;

	private FileManager fileManager;

	public Server() {
		eventHandler = new EventHandler(this);
		AbstractEvent.setHandler(eventHandler);

		settings = new Settings();

		fileManager = new FileManager(this);

		lobbyManager = new LobbyManager(this);
		playerManager = new PlayerManager(Constants.DEFAULT_MAX_PLAYERS, this);
		pluginManager = new PluginManager(this);
		boot();
	}

	private void boot() {
		init();
		running = true;
	}

	private void init() {
		registerPlugins();
		fileManager.init();
		this.port = settings.port;
	}

	public void create() {
		enet_server = Enet.createServer(port);
	}

	@Override
	public void run() {
		EnetEvent ev;

		if (handler == null) {
			return;
		}
		if (enet_server == null) {
			return;
		}
		log("Starting server..");
		startMs = System.nanoTime();
		while (running) {
			ev = Enet.service(enet_server);
			if (ev == null || ev.getData() == null) {
				continue;
			}
			try {
				handler.decode(ev, true);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Enet.destroyServer(enet_server);
		Enet.clean();
		down = true;
		log("Server down!");
	}

	public long getLastNS() {
		lastMS = System.nanoTime();
		return lastMS - startMs;
	}

	public void sendPacket(Peer peer, Packet pkt) {
		sendPacket(peer, pkt, true);
	}

	public void sendPacket(Peer peer, Packet pkt, boolean flush) {
		byte[] data = handler.encode(pkt);
		long h = Enet.enet_packet_create(data, data.length, 1);
		Enet.enet_peer_send(peer.getHandle(), (char) 0, h);
	}

	public void setHandler(DataHandler<EnetEvent, byte[], Packet> handler) {
		this.handler = handler;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isDown() {
		return down;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public EnetInstance getEnet_server() {
		return enet_server;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public PluginManager getPluginManager() {
		return pluginManager;
	}

	public LobbyManager getLobbyManager() {
		return lobbyManager;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public Settings getSettings() {
		return settings;
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public DataHandler<EnetEvent, byte[], Packet> getHandler() {
		return handler;
	}

	public void registerPlugins() {
		log("Loading plugins..");
		try {
			pluginManager.registerPlugin(ReloadPlugin.class);
			pluginManager.registerPlugin(OpPlugin.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void log(String msg) {
		System.out.println("[Server] "+msg);
	}
	
	public void error(String msg) {
		System.err.println("[Server] "+msg);
	}

	public static void main(String[] args) {
		Server server = new Server();

		server.setHandler(new PacketHandler(server));
		server.create();

		Thread network = new Thread(server);
		network.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (!server.isDown()) {
			try {
				if (!server.isRunning()) {
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String cmd = br.readLine();
				if (cmd == null) {
					return;
				}
				if (cmd.equals("stop")) {
					server.setRunning(false);
				} else {
					new ServerCommandEvent(cmd).fire();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
