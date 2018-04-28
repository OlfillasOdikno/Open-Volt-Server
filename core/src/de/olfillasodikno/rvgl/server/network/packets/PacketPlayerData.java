package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;
import de.olfillasodikno.rvgl.server.structures.Player;

public class PacketPlayerData implements Packet{
		
	private int id;

	private byte[] playername;
	private byte[] carname;
	
	private boolean cheat;
	
	private int crc;
	private int unknown;
	
	public PacketPlayerData() {}

	
	public PacketPlayerData(Player player) {
		this.id = player.getId();
		this.playername = player.getData().getPlayername();
		this.carname = player.getData().getCarData().getCarname();
		this.cheat = player.getData().isCheat();
	}
		
	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(id);
		buf.put(playername);
		buf.put(carname);
		buf.putInt(cheat?1:0);
		buf.putInt(1);
	}

	@Override
	public void decode(ByteBuffer buf) {
		unknown = buf.getInt();
		playername = new byte[16];
		buf.get(playername);
		carname = new byte[32];
		buf.get(carname);
		
		cheat = buf.getInt() == 0;
		if(buf.remaining()<4) {
			crc = 0;
		}else {
			crc = buf.getInt();			
		}
	}
	
	public int getId() {
		return id;
	}
	
	public int getCrc() {
		return crc;
	}
	
	public byte[] getCarname() {
		return carname;
	}
	
	public byte[] getPlayername() {
		return playername;
	}
	
	public boolean isCheat() {
		return cheat;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUnknown() {
		return unknown;
	}
}
