package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Game;
import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketGameStart implements Packet {

	private Game game;
	
	public PacketGameStart() {}
	
	public PacketGameStart(Game game) {
		this.game = game;
	}

	@Override
	public void encode(ByteBuffer buf) {
		buf.putInt(0);
		buf.putInt(game.getNumPlayers());
		buf.putInt(1);
		buf.putInt(game.getSettings().getMode()); //GAMEMODE?
		
		buf.putInt(game.getSettings().getLimit()); //RUNDEN
		buf.putInt(game.getSettings().isS() ? 1:0);
		buf.putInt(game.getSettings().isR() ? 1:0);

		buf.putInt(0); //RANDOM?
		
		buf.putInt(game.getSettings().isPickups() ? 1:0); //Pickups

		buf.putInt(game.getSettings().getNumCars()); //NUM CARS?		

		
		buf.putInt(game.getSettings().getDifficulty()); //IDK?		
		buf.putInt(game.getSettings().isRandomCars() ? 1:0);	
		buf.putInt(game.getSettings().isRandomTrack() ? 1:0);	

		buf.putShort((short)0); //RND?
		buf.putShort((short)game.getSettings().getLimit()); //RUNDEN?

		buf.putInt(game.getRestarts()+1); //IDK?
		
		buf.put(game.getTrackname(),0,16);
		for(Game.GamePlayerData data : game.getPlayerData().values()) {
			data.encode(buf);
		}
	}

	@Override
	public void decode(ByteBuffer buf) {}

}
