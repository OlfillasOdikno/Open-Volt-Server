package de.olfillasodikno.rvgl.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
import de.olfillasodikno.rvgl.server.utils.LogUtils;

public class Server implements Runnable {
	private static final String LOG_NAME = "Server";

	private static final Logger logger = Logger.getLogger(Server.class.getName());
	static {
		Logger mainLogger = Logger.getLogger("de.olfillasodikno.");
		mainLogger.setUseParentHandlers(false);
		ConsoleHandler cHandler = new ConsoleHandler();
		cHandler.setFormatter(new SimpleFormatter() {
			private static final String FORMAT = "[%1$tF %1$tT] %2$s %n";

			@Override
			public synchronized String format(LogRecord record) {
				String msg = record.getMessage();
				if (record.getParameters() != null) {
					MessageFormat msgFormat = new MessageFormat(msg);
					msg = msgFormat.format(record.getParameters());
				}
				return String.format(FORMAT, new Date(record.getMillis()), msg);
			}
		});
		mainLogger.addHandler(cHandler);
	}

	private DataHandler<EnetEvent, byte[], Packet> handler;

	private boolean running;
	private boolean down;

	private short port;

	private long startMs;

	private EnetInstance enetServer;

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
		this.port = settings.getPort();
	}

	public void create() {
		try {
			enetServer = Enet.createServer(port);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e.getCause());
		}
	}

	@Override
	public void run() {
		EnetEvent ev;

		if (handler == null) {
			return;
		}
		if (enetServer == null || enetServer.getHostHandle() == 0) {
			error("Failed to start Server (maybe port is already in use?)");
			return;
		}
		log("Starting server..");
		startMs = System.nanoTime();
		while (running) {
			ev = Enet.service(enetServer);
			if (ev == null || ev.getData() == null) {
				continue;
			}
			try {
				handler.decode(ev, true);
			} catch (InstantiationException | IllegalAccessException e) {
				logger.log(Level.SEVERE, e.getMessage(), e.getCause());
			}
		}
		Enet.destroyServer(enetServer);
		Enet.clean();
		down = true;
		log("Server down!");
	}

	public long getLastNS() {
		return System.nanoTime() - startMs;
	}

	public void sendPacket(Peer peer, Packet pkt) {
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

	public EnetInstance getEnetServer() {
		return enetServer;
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
			logger.log(Level.SEVERE, e.getMessage(), e.getCause());
		}
	}

	public void log(String msg, Object... args) {
		LogUtils.log(logger, Level.INFO, LOG_NAME, msg, args);
	}

	public void error(String msg, Object... args) {
		LogUtils.log(logger, Level.SEVERE, LOG_NAME, msg, args);
	}

	public static void main(String[] args){
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
						logger.log(Level.SEVERE, e.getMessage(), e.getCause());
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
				logger.log(Level.SEVERE, e1.getMessage(), e1.getCause());
			}
		}
		logger.info("Have a nice day!");
	}

}
