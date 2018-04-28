package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketPlayerDisconnect implements Packet{

	private int id;
	
	public PacketPlayerDisconnect() {}
	public PacketPlayerDisconnect(int id) {
		this.id = id;
	}
	
	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(id);
	}

	@Override
	public void decode(ByteBuffer buf) {
		id = buf.getInt();
	}
	
	public void setId(int id) {
		this.id = id;
	}

}
