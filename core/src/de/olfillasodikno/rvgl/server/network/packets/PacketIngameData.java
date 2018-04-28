package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import de.olfillasodikno.rvgl.server.structures.Packet;
import de.olfillasodikno.rvgl.server.structures.ingame.IngameObject;

public class PacketIngameData implements Packet {

	private short subID;

	private ArrayList<IngameObject> objects;

	public PacketIngameData() {
	}
	
	public PacketIngameData(ArrayList<IngameObject> objects) {
		this.objects = objects;
		this.subID = (short) objects.size();
	}

	@Override
	public void setSubID(short id) {
		this.subID = id;
	}

	@Override
	public short getSubID() {
		return subID;
	}
	
	public ArrayList<IngameObject> getObjects() {
		return objects;
	}

	@Override
	public void encode(ByteBuffer buf) {
		subID = (short) objects.size();
		for (int i = 0; i < objects.size(); i++) {
			IngameObject obj = objects.get(i);
			if (obj != null) {
				obj.encode(buf);
			}
		}
	}

	@Override
	public void decode(ByteBuffer buf) {
		int size = subID;
		objects = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			IngameObject obj = IngameObject.decode(buf);
			if (obj != null) {
				objects.add(obj);
			}
			if(!buf.hasRemaining()) {
				break;
			}
		}
	}

}
