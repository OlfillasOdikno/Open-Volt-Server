package de.olfillasodikno.libenet;

public class Enet {

	static {
		System.loadLibrary("jnienet");
		enet_initialize();
	}
	
	public static EnetInstance createClient() {
		long handle = enet_host_create("", (short)0, 1, 2, 57600 / 8, 14400 / 8);
		return new EnetInstance(handle);
	}
	
	public static EnetInstance createServer(short port) {
		long handle = enet_host_create("0", port, 100, 0, 57600 / 8, 14400 / 8);
		return new EnetInstance(handle);
	}
	
	public static void destroyServer(EnetInstance server) {
		enet_host_destroy(server.getHostHandle());
	}
	
	public static long connectInstance(EnetInstance instance, String host, int port) {
		long h = connect(instance.getHostHandle(), host, port,2);
		if(h == 0) {
			System.err.println("Failed to connect to instance");
			return -1;
		}
		instance.addConnection(h);
		return h;
	}
	
	public static EnetEvent service(EnetInstance instance) {
		long h = enet_host_service(instance.getHostHandle(), 5000);
		if(h!=0) {
			byte[] data = getEvent(h);
			return new EnetEvent(data);	
		}
		return null;
	}
	
	public static void clean() {
		enet_deinitialize();
	}
	
	private static native int enet_initialize();

	private static native void enet_deinitialize();

	private static native long enet_host_create(String host, short port, int outCon, int ch, int upstream, int downstream);

	private static native void enet_host_destroy(long hostHandle);

	private static native long connect(long hostHandle, String host, int port, int ch);

	private static native long enet_host_service(long hostHandle, int timeout);

	private static native byte[] getEvent(long eventHandle);

	public static native long enet_packet_create(byte[] data, int length, int flag);

	private static native void enet_packet_destroy(long packetHandle);

	public static native void enet_peer_send(long peerHandle, char ch, long packetHandle);

	public static native void enet_host_flush(long hostHandle);

	public static native void enet_host_broadcast(long hostHandle, char ch, long packetHandle);
	
	public static native byte[] getAddress(long peerHandle);
	
	public static native void enet_peer_destroy(long peerHandle);

}
