package de.olfillasodikno.libenet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class EnetEvent {
	public static final int ENET_EVENT_TYPE_CONNECT = 1;
	public static final int ENET_EVENT_TYPE_DISCONNECT = 2;
	public static final int ENET_EVENT_TYPE_RECEIVE = 3;

	private final int type;
	private final int datalength;
	private final long peerHandle;
	private final byte[] data;

	public EnetEvent(byte[] ev) {
		ByteBuffer buff = ByteBuffer.wrap(ev);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		type = buff.getInt();
		datalength = buff.getInt();
		peerHandle = buff.getLong();
		data = Arrays.copyOfRange(ev, 16, ev.length);
	}

	public int getType() {
		return type;
	}

	public int getDatalength() {
		return datalength;
	}

	public long getPeerHandle() {
		return peerHandle;
	}

	public byte[] getData() {
		return data;
	}
}
