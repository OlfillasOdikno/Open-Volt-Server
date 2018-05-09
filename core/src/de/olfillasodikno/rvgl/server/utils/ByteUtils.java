package de.olfillasodikno.rvgl.server.utils;

public class ByteUtils {
	
	private ByteUtils() {}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < bytes.length; j++) {
			byte b = (byte) (bytes[j] & (byte) 0xFF);
			sb.append(String.format("%02X ", b));
		}
		return sb.toString();
	}
}
