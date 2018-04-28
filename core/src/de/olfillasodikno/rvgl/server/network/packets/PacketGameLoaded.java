package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketGameLoaded implements Packet{
	
	private int restartCount;
	private int car_checksum;
	private int level_checksum;
	private int unknown;
	
	public PacketGameLoaded() {}
		
	@Override
	public void encode(ByteBuffer buf) {
		
		buf.putInt(restartCount);
		buf.putInt(car_checksum);
		buf.putInt(level_checksum);
		buf.putInt(unknown);

	}

	@Override
	public void decode(ByteBuffer buf) {
		restartCount = buf.getInt();
		car_checksum = buf.getInt();
		level_checksum = buf.getInt();
		unknown = buf.getInt();
	}

	public int getRestartCount() {
		return restartCount;
	}

	public void setRestartCount(int restartCount) {
		this.restartCount = restartCount;
	}

	public int getCar_checksum() {
		return car_checksum;
	}

	public void setCar_checksum(int car_checksum) {
		this.car_checksum = car_checksum;
	}

	public int getLevel_checksum() {
		return level_checksum;
	}

	public void setLevel_checksum(int level_checksum) {
		this.level_checksum = level_checksum;
	}

	public int getUnknown() {
		return unknown;
	}

	public void setUnknown(int unknown) {
		this.unknown = unknown;
	}

}
