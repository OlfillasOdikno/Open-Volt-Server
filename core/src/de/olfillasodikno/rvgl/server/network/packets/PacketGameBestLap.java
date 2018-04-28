package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketGameBestLap implements Packet{
	
	private int timeMS; //Time of best lap
	private int id;

	public PacketGameBestLap() {}
		
	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(timeMS);
		buf.putInt(id);
		buf.putInt(1);
	}

	@Override
	public void decode(ByteBuffer buf) {
		timeMS = buf.getInt();
		id = buf.getInt();
	}

	public int getTimeMS() {
		return timeMS;
	}

	public void setTimeMS(int timeMS) {
		this.timeMS = timeMS;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
