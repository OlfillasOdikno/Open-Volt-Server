package de.olfillasodikno.rvgl.server;

public class Constants {
	
	private Constants() {}

	public static final short DEFAULT_PORT = 2310;
	protected static final byte[] RVGL_GUID = new byte[] { (byte)0x73, (byte)0xDA, (byte)0x5C, (byte)0x64, (byte)0xDC, (byte)0x46, (byte)0x5E, (byte)0xC0, (byte)0x40, (byte)0xAC,
			(byte)0x32, (byte)0xAE, (byte)0xFB, (byte)0x25, (byte)0xB2, (byte)0x59 };
	public static final short DEFAULT_MAX_PLAYERS = 16;
	
	public static final String PLUGIN_CONFIG_FILE = "plugin.json";
	public static final String DEFAULT_CONFIG_FILE = "config.json";

	public static final boolean DEV = false;
	
	public static final String COMMAND_PREFIX = "#";
}
