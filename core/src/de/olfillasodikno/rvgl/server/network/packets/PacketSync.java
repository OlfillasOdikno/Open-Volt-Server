package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketSync implements Packet{
	
	private long count;

	public PacketSync() {}
		
	@Override
	public void encode(ByteBuffer buf) {
		buf.putLong(count);
	}

	@Override
	public void decode(ByteBuffer buf) {
		count = buf.getLong();
	}
	
	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}

}
