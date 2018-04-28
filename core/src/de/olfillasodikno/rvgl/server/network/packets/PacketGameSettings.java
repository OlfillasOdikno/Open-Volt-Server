package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.GameSettings;
import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketGameSettings implements Packet {

	private byte mode;
	private byte difficulty;
	private byte num_cars;
	private byte limit;
	private byte[] trackname;

	private boolean r;
	private boolean s;
	private boolean randomCars;
	private boolean randomTrack;
	private boolean pickups;
	private boolean inProgress;

	public PacketGameSettings() {
	}

	public PacketGameSettings(GameSettings settings) {
		this.mode = settings.getMode();
		this.difficulty = settings.getDifficulty();
		this.num_cars = settings.getNum_cars();
		this.limit = settings.getLimit();
		this.trackname = settings.getTrackname();
		this.r = settings.isR();
		this.s = settings.isS();
		this.randomCars = settings.isRandomCars();
		this.randomTrack = settings.isRandomTrack();
		this.pickups = settings.isPickups();
	}

	@Override
	public void encode(ByteBuffer buf) {
		buf.put(mode);
		buf.put(difficulty);
		buf.put(num_cars);
		buf.put(limit);
		buf.put(trackname, 0, 16);
		buf.put((byte) (r ? 1 : 0));
		buf.put((byte) (s ? 1 : 0));
		buf.put((byte) (randomCars ? 1 : 0));
		buf.put((byte) (randomTrack ? 1 : 0));
		buf.put((byte) (pickups ? 1 : 0));
		buf.put((byte) (inProgress ? 1 : 0));
		buf.putShort((short)0); //UNKNOWN
	}

	@Override
	public void decode(ByteBuffer buf) {}

	public byte getMode() {
		return mode;
	}

	public void setMode(byte mode) {
		this.mode = mode;
	}

	public byte getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(byte difficulty) {
		this.difficulty = difficulty;
	}

	public byte getNum_cars() {
		return num_cars;
	}

	public void setNum_cars(byte num_cars) {
		this.num_cars = num_cars;
	}

	public byte getLimit() {
		return limit;
	}

	public void setLimit(byte limit) {
		this.limit = limit;
	}

	public byte[] getTrackname() {
		return trackname;
	}

	public boolean isRandomCars() {
		return randomCars;
	}

	public void setRandomCars(boolean randomCars) {
		this.randomCars = randomCars;
	}

	public boolean isRandomTrack() {
		return randomTrack;
	}

	public void setRandomTrack(boolean randomTrack) {
		this.randomTrack = randomTrack;
	}

	public boolean isPickups() {
		return pickups;
	}

	public void setPickups(boolean pickups) {
		this.pickups = pickups;
	}
	
	public boolean isR() {
		return r;
	}
	
	public boolean isS() {
		return s;
	}
	
	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}
	
	public boolean isInProgress() {
		return inProgress;
	}

}
