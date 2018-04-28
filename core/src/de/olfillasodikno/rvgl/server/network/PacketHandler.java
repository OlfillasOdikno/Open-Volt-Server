package de.olfillasodikno.rvgl.server.network;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.olfillasodikno.libenet.Enet;
import de.olfillasodikno.libenet.EnetEvent;
import de.olfillasodikno.libenet.Peer;
import de.olfillasodikno.rvgl.server.Constants;
import de.olfillasodikno.rvgl.server.Server;
import de.olfillasodikno.rvgl.server.events.PrePacketEvent;
import de.olfillasodikno.rvgl.server.events.PlayerDisconnectEvent;
import de.olfillasodikno.rvgl.server.events.PlayerFinishEvent;
import de.olfillasodikno.rvgl.server.events.PlayerJoinEvent;
import de.olfillasodikno.rvgl.server.events.PlayerReadyEvent;
import de.olfillasodikno.rvgl.server.events.PostPacketEvent;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameBestLap;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameBomb;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameCountdown;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameFinish;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameLoaded;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameSettings;
import de.olfillasodikno.rvgl.server.network.packets.PacketGameStart;
import de.olfillasodikno.rvgl.server.network.packets.PacketIngameData;
import de.olfillasodikno.rvgl.server.network.packets.PacketItemSlot;
import de.olfillasodikno.rvgl.server.network.packets.PacketItemUse;
import de.olfillasodikno.rvgl.server.network.packets.PacketSync;
import de.olfillasodikno.rvgl.server.network.packets.PacketSyncReply;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerAdddress;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerChat;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerData;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerDisconnect;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerId;
import de.olfillasodikno.rvgl.server.network.packets.PacketPlayerReady;
import de.olfillasodikno.rvgl.server.network.packets.PacketRequestPlayerId;
import de.olfillasodikno.rvgl.server.network.packets.PacketStarGet;
import de.olfillasodikno.rvgl.server.network.packets.PacketStarUse;
import de.olfillasodikno.rvgl.server.structures.BiHashMap;
import de.olfillasodikno.rvgl.server.structures.CarData;
import de.olfillasodikno.rvgl.server.structures.Game;
import de.olfillasodikno.rvgl.server.structures.Game.GamePlayerData;
import de.olfillasodikno.rvgl.server.structures.Packet;
import de.olfillasodikno.rvgl.server.structures.Player;
import de.olfillasodikno.rvgl.server.utils.ByteUtils;

public class PacketHandler implements DataHandler<EnetEvent, byte[], Packet> {

	private static final BiHashMap<Short, Class<? extends Packet>> packets = new BiHashMap<>();
	static {
		packets.put((short) 0x01, PacketIngameData.class);

		packets.put((short) 0x02, PacketPlayerAdddress.class);
		packets.put((short) 0x03, PacketRequestPlayerId.class);
		packets.put((short) 0x04, PacketPlayerDisconnect.class);
		packets.put((short) 0x05, PacketPlayerId.class);
		packets.put((short) 0x06, PacketPlayerData.class);
		packets.put((short) 0x07, PacketPlayerReady.class);

		packets.put((short) 0x0c, PacketGameSettings.class);
		packets.put((short) 0x0d, PacketGameStart.class);
		packets.put((short) 0x0e, PacketGameLoaded.class);

		packets.put((short) 0x0f, PacketSync.class);
		packets.put((short) 0x10, PacketSyncReply.class);

		packets.put((short) 0x11, PacketGameCountdown.class);
		packets.put((short) 0x12, PacketGameBestLap.class);
		packets.put((short) 0x13, PacketGameFinish.class);

		packets.put((short) 0x17, PacketItemUse.class);
		packets.put((short) 0x18, PacketItemSlot.class);
		packets.put((short) 0x19, PacketStarGet.class);
		packets.put((short) 0x1A, PacketStarUse.class);

		packets.put((short) 0x1B, PacketGameBomb.class);

		packets.put((short) 0x1e, PacketPlayerChat.class);
	}

	private ByteBuffer transbuffer;

	private Server server;

	public PacketHandler(Server server) {
		this.server = server;
		transbuffer = ByteBuffer.allocate(1024);
		transbuffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	@Override
	public Packet decode(EnetEvent ev, boolean handle) throws InstantiationException, IllegalAccessException {
		if (ev.getType() == EnetEvent.ENET_EVENT_TYPE_CONNECT) {
			return null;
		} else if (ev.getType() == EnetEvent.ENET_EVENT_TYPE_DISCONNECT) {
			long peerHandle = ev.getPeerHandle();
			Player player = server.getPlayerManager().getPlayer(peerHandle);
			if (player == null) {
				return null;
			}
			player.getCurrentLobby().removePlayer(player);
			new PlayerDisconnectEvent(player).fire();
			server.getPlayerManager().removePlayer(player);
			return null;
		}
		if (ev.getType() != EnetEvent.ENET_EVENT_TYPE_RECEIVE) {
			return null;
		}
		byte[] data = ev.getData();
		if (data.length < 8) {
			return null;
		}

		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		short id = buf.getShort();
		short subid = buf.getShort();
		if (!packets.hasK(id)) {

			if (Constants.DEV) {
				server.log("------------------------");
				server.log("NEW PACKET");
				server.log(ByteUtils.bytesToHex(data));
				server.log("------------------------");
				server.log("\n");
			}

			return null;
		}
		Packet packet = packets.getV(id).newInstance();
		packet.setSubID(subid);
		try {
			packet.decode(buf);
		} catch (BufferUnderflowException e) {
			if (Constants.DEV) {
				server.error("INVALID DATA");
			}
			Enet.enet_peer_destroy(ev.getPeerHandle());
			return null;
		}
		if (handle) {
			handle(ev, packet);
		}
		return packet;
	}

	@Override
	public byte[] encode(Packet obj) {
		transbuffer.clear();
		if (!packets.hasV(obj.getClass())) {
			if (Constants.DEV) {
				server.error("There is no id for Packet: " + obj.getClass().getSimpleName());
			}
			return null;
		}
		short id = (short) packets.getK(obj.getClass());
		transbuffer.putShort(id);
		transbuffer.putShort(obj.getSubID());
		obj.encode(transbuffer);
		byte[] out = new byte[transbuffer.position()];
		transbuffer.position(0);
		transbuffer.get(out);
		return out;
	}

	@Override
	public void handle(EnetEvent ev, Packet obj) {
		long peerHandle = ev.getPeerHandle();
		Player player = server.getPlayerManager().getPlayer(peerHandle);
		PrePacketEvent event = new PrePacketEvent(obj, player);
		event.fire();
		if (event.isCanceled()) {
			return;
		}
		if (obj instanceof PacketRequestPlayerId) {
			if (player == null) {
				Peer peer = new Peer(peerHandle);
				player = server.getPlayerManager().getPlayer(peer);

				PacketPlayerId id = new PacketPlayerId(player.getId());
				byte[] data = encode(id);
				long packetHandle = Enet.enet_packet_create(data, data.length, 1);
				Enet.enet_peer_send(peerHandle, (char) 0, packetHandle);

				PacketRequestPlayerId reqPkt = new PacketRequestPlayerId();
				reqPkt.setId(player.getId());
				player.sendPacket(reqPkt);

				server.getLobbyManager().getHub().addPlayer(player, true);

			} else {
				player = server.getPlayerManager().getPlayer(peerHandle);

				PacketPlayerId id = new PacketPlayerId(player.getId());
				byte[] data = encode(id);
				long packetHandle = Enet.enet_packet_create(data, data.length, 1);
				Enet.enet_peer_send(peerHandle, (char) 0, packetHandle);

				PacketRequestPlayerId reqPkt = new PacketRequestPlayerId();
				reqPkt.setId(player.getId());
				player.sendPacket(reqPkt);

				server.getLobbyManager().getHub().addPlayer(player, true);
			}
			new PlayerJoinEvent(player).fire();
		} else if (obj instanceof PacketPlayerData) {
			if (player != null) {
				PacketPlayerData playerDataPkt = (PacketPlayerData) obj;

				Player.Data playerData = player.getData();
				playerData.setPlayername(playerDataPkt.getPlayername());
				playerData.setCheat(playerDataPkt.isCheat());

				CarData carData = playerData.getCarData();
				carData.setCarname(playerDataPkt.getCarname());
				carData.setCrc(playerDataPkt.getCrc());

				playerDataPkt.setId(player.getId());

				player.getCurrentLobby().broadcast(playerDataPkt);
			}

		} else if (obj instanceof PacketPlayerReady) {
			if (player != null) {
				PacketPlayerReady playerReadyPkt = (PacketPlayerReady) obj;
				playerReadyPkt.setId(player.getId());

				player.getData().setReady(playerReadyPkt.isReady());
				player.getData().setReady(playerReadyPkt.isSpectator());

				PlayerReadyEvent rev = new PlayerReadyEvent(player);
				rev.fire();

				player.getCurrentLobby().broadcast(playerReadyPkt);
			}
		} else if (obj instanceof PacketPlayerChat) {
			if (player != null) {
				PacketPlayerChat playerChatPkt = (PacketPlayerChat) obj;
				playerChatPkt.setId(player.getId());
				String[] dat = playerChatPkt.getMsg().split(": ", 2);
				if (dat.length == 2 && dat[1].startsWith("#")) {
					server.getPluginManager().parseHandleCmd(dat[1], player);
				} else {
					player.getCurrentLobby().broadcast(playerChatPkt, player, true);
				}
			}
		} else if (obj instanceof PacketSync) {
			if (player != null) {
				PacketSync syncPkt = (PacketSync) obj;

				PacketSyncReply replPkt = new PacketSyncReply();
				replPkt.setCountA(syncPkt.getCount());
				replPkt.setCountB(server.getLastNS());
				player.sendPacket(replPkt);
			}
		} else if (obj instanceof PacketIngameData) {
			if (player != null) {
				PacketIngameData pkt = (PacketIngameData) obj;
				Game game = player.getCurrentLobby().getGame();
				game.updateGameObjects(pkt, player);
			}
		} else if (obj instanceof PacketGameLoaded) {
			if (player != null) {
				// PacketGameLoaded pkt = (PacketGameLoaded) obj;
				Game game = player.getCurrentLobby().getGame();
				GamePlayerData data = game.getPlayerData().get(player);
				if (data != null) {
					data.setLoaded(true);
				}
			}
		} else if (obj instanceof PacketGameFinish) {
			if (player != null) {
				PacketGameFinish pkt = (PacketGameFinish) obj;
				pkt.setId(player.getId());
				player.getCurrentLobby().broadcast(pkt);
				Game game = player.getCurrentLobby().getGame();
				GamePlayerData data = game.getPlayerData().get(player);
				data.setFinished(true);
				new PlayerFinishEvent(player).fire();
			}
		} else if (obj instanceof PacketItemUse) {
			if (player != null) {
				PacketItemUse pkt = (PacketItemUse) obj;
				player.getCurrentLobby().broadcast(pkt);

			}
		} else if (obj instanceof PacketItemSlot) {
			if (player != null) {
				PacketItemSlot pkt = (PacketItemSlot) obj;
				player.getCurrentLobby().broadcast(pkt);
			}
		} else if (obj instanceof PacketStarGet) {
			if (player != null) {
				PacketStarGet pkt = (PacketStarGet) obj;
				player.getCurrentLobby().broadcast(pkt);
			}
		} else if (obj instanceof PacketStarUse) {
			if (player != null) {
				PacketStarUse pkt = (PacketStarUse) obj;
				player.getCurrentLobby().broadcast(pkt);
			}
		} else if (obj instanceof PacketGameBomb) {
			if (player != null) {
				PacketGameBomb pkt = (PacketGameBomb) obj;
				player.getCurrentLobby().broadcast(pkt);
			}
		} else if (obj instanceof PacketPlayerDisconnect) {
			if (player != null) {
				player.getCurrentLobby().removePlayer(player);
				new PlayerDisconnectEvent(player).fire();
			}
		}
		new PostPacketEvent(obj, player).fire();
	}

}
