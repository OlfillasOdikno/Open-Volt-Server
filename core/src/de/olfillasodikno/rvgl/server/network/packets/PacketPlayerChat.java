package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketPlayerChat implements Packet{
		
	private int id;
	private String msg;
	
	private final int maxChat = 500;
	
	public PacketPlayerChat() {}
	
	public PacketPlayerChat(int id,String msg) {
		this.id = id;
		this.msg = msg;
	}
		
	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(id);
		buf.put(msg.getBytes());
		buf.put((byte) 0);
	}

	@Override
	public void decode(ByteBuffer buf) {
		id = buf.getInt();
		int length = Math.min(buf.remaining(),maxChat);
		byte[] data = new byte[length];
		buf.get(data);
		msg = new String(data);
	}
	
	public int getId() {
		return id;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
