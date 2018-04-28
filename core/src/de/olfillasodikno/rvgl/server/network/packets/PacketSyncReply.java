package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketSyncReply implements Packet{
	
	private long countA;
	private long countB;

	public PacketSyncReply() {}
		
	@Override
	public void encode(ByteBuffer buf) {
		buf.putLong(countA);
		buf.putLong(countB);
	}

	@Override
	public void decode(ByteBuffer buf) {
		countA = buf.getLong();
		countB = buf.getLong();
	}
	
	public long getCountA() {
		return countA;
	}
	
	public long getCountB() {
		return countB;
	}
	
	public void setCountA(long countA) {
		this.countA = countA;
	}
	
	public void setCountB(long countB) {
		this.countB = countB;
	}

}
