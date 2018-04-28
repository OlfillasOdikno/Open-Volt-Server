package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketStarUse implements Packet {

	private int id;


	public PacketStarUse() {
	}

	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(id);

		buf.putInt(1);
	}

	@Override
	public void decode(ByteBuffer buf) {
		id = buf.getInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
