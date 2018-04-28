package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketPlayerReady implements Packet {

	private int id;
	private boolean ready;
	private boolean spectator;

	public PacketPlayerReady() {
	}

	public PacketPlayerReady(int id, boolean ready, boolean spectator) {
		this.id = id;
		this.ready = ready;
		this.spectator = spectator;
	}

	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(ready ? 1 : 0);
		buf.putInt(spectator ? 1 : 0);
		buf.putInt(id);
	}

	@Override
	public void decode(ByteBuffer buf) {
		ready = buf.getInt() != 0;
		spectator = buf.getInt() != 0;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public boolean isSpectator() {
		return spectator;
	}

	public boolean isReady() {
		return ready;
	}

}
