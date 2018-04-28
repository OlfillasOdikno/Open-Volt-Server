package de.olfillasodikno.rvgl.server.structures;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public interface Packet {

	public void encode(ByteBuffer buf);

	public void decode(ByteBuffer buf) throws BufferUnderflowException;

	public default void setSubID(short id) {
	}

	public default short getSubID() {
		return (short) 0;
	}
}
