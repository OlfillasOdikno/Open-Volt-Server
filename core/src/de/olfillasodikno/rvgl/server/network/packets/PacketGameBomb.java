package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketGameBomb implements Packet {

	private int unknown;

	private int from1;
	private int from2;

	private int to;
	
	private int unknown2;
	
	public PacketGameBomb() {
	}

	@Override
	public void encode(ByteBuffer buf) {
		
		buf.putInt(unknown);
		buf.putInt(from1);
		buf.putInt(from2);
		buf.putInt(to);
		
		buf.putInt(1);
		
		buf.putInt(unknown2);

	}

	@Override
	public void decode(ByteBuffer buf) {
		unknown = buf.getInt();
		
		from1 = buf.getInt();
		from2 = buf.getInt();
		
		to = buf.getInt();
		
		unknown2 = buf.getInt();
		
	}

}
