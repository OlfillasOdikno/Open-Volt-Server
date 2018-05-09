package de.olfillasodikno.rvgl.server.structures.ingame;

import java.nio.ByteBuffer;
import java.util.HashMap;

import de.olfillasodikno.rvgl.server.Constants;

public abstract class IngameObject {

	private static final HashMap<Short, Class<? extends IngameObject>> map = new HashMap<>();
	static {
		map.put((short) 0x15, IngamePlayer.class);
		// map.put((short) 0x16, IngameOther.class);
		map.put((short) 0x1D, IngameHorn.class);
	}

	private int length;
	private short type;
	private short subType;

	public void encode(ByteBuffer buf) {
		encodeHead(buf);
		encodeBody(buf);
	}

	public abstract void encodeBody(ByteBuffer buf);

	public void encodeHead(ByteBuffer buf) {
		buf.putInt(length);
		buf.putShort(type);
		buf.putShort(subType);
	}

	public static IngameObject decode(ByteBuffer buf) {
		int length = buf.getInt();
		int oldPos = buf.position();
		if(length>1024) {
			buf.position(buf.limit()-1);
			return null;
		}
		if (length < 4) {
			buf.position(oldPos + length);
			return null;
		}
		short type = buf.getShort();
		short subType = buf.getShort();
		IngameObject obj;
		if (!map.containsKey(type)) {
			if(Constants.DEV) {
				System.err.println("No obj with type: " + Integer.toHexString(type));				
			}

			obj = new IngameUnknownObject(length);
			obj.type = type;
			obj.subType = subType;
			obj.length = length;
			obj.decodeBody(buf);
			return obj;
		}
		try {
			obj = map.get(type).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			buf.position(oldPos + length);
			return null;
		}
		obj.type = type;
		obj.subType = subType;
		obj.length = length;
		obj.decodeBody(buf);
		return obj;
	}

	public short getType() {
		return type;
	}

	public short getSubType() {
		return subType;
	}
	
	public abstract void decodeBody(ByteBuffer buf);

}
