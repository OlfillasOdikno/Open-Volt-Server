package de.olfillasodikno.libenet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Peer {
	
	private final long handle;
	
	private byte[] addr;
	private String host;
	private int port;
	
	public Peer(long handle) {
		this.handle = handle;
		port = -1;
	}
	
	public long getHandle() {
		return handle;
	}
	
	public byte[] getAddress() {
		if(addr == null) {
			addr = Enet.getAddress(handle);
		}
		return addr;
	}
	
	public String getHost() {
		if(host == null) {
			convert();
		}
		return host;
	}
	
	public int getPort() {
		if(port == -1) {
			convert();
		}
		return port;
	}
	
	private void convert() {
		ByteBuffer buffer = ByteBuffer.wrap(getAddress());
		byte[] h = new byte[4];
		buffer.get(h);
		try {
			host = InetAddress.getByAddress(h).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		port = buffer.getInt();
	}
}
