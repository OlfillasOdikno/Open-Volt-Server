package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketGameLoaded implements Packet{
	
	private int restartCount;
	private int carChecksum;
	private int levelChecksum;
	private int unknown;
	
	public PacketGameLoaded() {}
		
	@Override
	public void encode(ByteBuffer buf) {
		
		buf.putInt(restartCount);
		buf.putInt(carChecksum);
		buf.putInt(levelChecksum);
		buf.putInt(unknown);

	}

	@Override
	public void decode(ByteBuffer buf) {
		restartCount = buf.getInt();
		carChecksum = buf.getInt();
		levelChecksum = buf.getInt();
		unknown = buf.getInt();
	}

	public int getRestartCount() {
		return restartCount;
	}

	public int getCarChecksum() {
		return carChecksum;
	}

	public void setCarChecksum(int carChecksum) {
		this.carChecksum = carChecksum;
	}

	public int getLevelChecksum() {
		return levelChecksum;
	}

	public void setLevelChecksum(int levelChecksum) {
		this.levelChecksum = levelChecksum;
	}

	public void setRestartCount(int restartCount) {
		this.restartCount = restartCount;
	}

	public int getUnknown() {
		return unknown;
	}

	public void setUnknown(int unknown) {
		this.unknown = unknown;
	}

}
