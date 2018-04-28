package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketRequestPlayerId implements Packet{

	private byte[] magic;
	private byte[] unknown;
	
	private int id;
	
	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(id);
	}

	@Override
	public void decode(ByteBuffer buf) {
		magic = new byte[16];
		buf.get(magic, 0, magic.length);
		unknown = new byte[buf.remaining()];
		buf.get(unknown, 0, unknown.length);
	}
	
	public byte[] getMagic() {
		return magic;
	}
	
	public byte[] getUnknown() {
		return unknown;
	}
	
	public void setId(int id) {
		this.id = id;
	}

}
