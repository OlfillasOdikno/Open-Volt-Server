package de.olfillasodikno.rvgl.server.structures.ingame;

import java.nio.ByteBuffer;

public class IngameHorn extends IngameObject {

	private int id;

	@Override
	public void encodeBody(ByteBuffer buf) {
		buf.putInt(id);
	}

	@Override
	public void decodeBody(ByteBuffer buf) {
		id = buf.getInt();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
