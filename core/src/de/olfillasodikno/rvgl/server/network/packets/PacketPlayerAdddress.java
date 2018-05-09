package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;
import de.olfillasodikno.rvgl.server.structures.Player;

public class PacketPlayerAdddress implements Packet{
	
	private byte[] addr;
	
	private int id;

	private Player player;
	
	public PacketPlayerAdddress() {}
	
	public PacketPlayerAdddress(Player player) {
		this.player = player;
	}
		
	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(player.getId());
		buf.put(player.getPeer().getAddress());
	}

	@Override
	public void decode(ByteBuffer buf) {
		addr = new byte[8];
		id = buf.getInt();
		buf.get(addr);
	}
	
	public byte[] getAddr() {
		return addr;
	}
	
	public int getId() {
		return id;
	}
}
