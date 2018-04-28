package de.olfillasodikno.rvgl.server.structures.ingame;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.Constants;
import de.olfillasodikno.rvgl.server.utils.ByteUtils;

public class IngameUnknownObject extends IngameObject{

	private byte[] remaining;
	
	public IngameUnknownObject(int length) {
		remaining = new byte[length-4];
	}
	
	@Override
	public void encodeBody(ByteBuffer buf) {
		buf.put(remaining);
	}

	@Override
	public void decodeBody(ByteBuffer buf) {
		buf.get(remaining);
		if(Constants.DEV) {
			System.out.println(ByteUtils.bytesToHex(remaining));			
		}
	}

}
