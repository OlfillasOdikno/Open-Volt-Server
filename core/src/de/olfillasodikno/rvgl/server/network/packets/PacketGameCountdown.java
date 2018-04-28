package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketGameCountdown implements Packet {

	private long time;
	private long frequency;

	public PacketGameCountdown() {
	}

	public PacketGameCountdown(long time,long frequency) {
		this.time = time;
		this.frequency = frequency;
	}

	@Override
	public void encode(ByteBuffer buf) {
		// buf.putLong(1232334); //StartTime??

		buf.putLong(time);

		buf.putLong(frequency);

	}

	@Override
	public void decode(ByteBuffer buf) {
	}

}
