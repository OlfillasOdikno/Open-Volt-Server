package de.olfillasodikno.rvgl.server.structures;

import java.util.Arrays;

public class GameSettings {

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

	public GameSettings() {
		trackname = new byte[16];
		setTrackname("nhood1".getBytes());
		setNum_cars((byte) 8);
		setMode((byte)4);
		setLimit((byte)3);
		setPickups(true);
	}

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

	public void setTrackname(byte[] trackname) {
		if (trackname.length == this.trackname.length) {
			this.trackname = trackname;
		} else {
			Arrays.fill(this.trackname, (byte)0);
			System.arraycopy(trackname, 0, this.trackname, 0, Math.min(trackname.length, this.trackname.length));
		}
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

	public void setR(boolean r) {
		this.r = r;
	}

	public boolean isS() {
		return s;
	}

	public void setS(boolean s) {
		this.s = s;
	}
}
