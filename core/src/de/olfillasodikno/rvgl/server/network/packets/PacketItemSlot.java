package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketItemSlot implements Packet {

	private int id;

	private int item;

	public PacketItemSlot() {
	}

	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(item);
		buf.putInt(id);

		buf.putInt(1);
	}

	@Override
	public void decode(ByteBuffer buf) {
		item = buf.getInt();
		id = buf.getInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

}
