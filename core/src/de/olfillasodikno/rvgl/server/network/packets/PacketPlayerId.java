package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketPlayerId implements Packet{
	
	private int id;

	public PacketPlayerId() {}
	
	public PacketPlayerId(int id) {
		this.id = id;
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

}
