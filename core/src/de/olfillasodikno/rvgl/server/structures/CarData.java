package de.olfillasodikno.rvgl.server.structures;

public class CarData {

	private int crc;
	private byte[] carname = new byte[32];

	public int getCrc() {
		return crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	public byte[] getCarname() {
		return carname;
	}

	public void setCarname(byte[] carname) {
		if (carname.length == this.carname.length) {
			this.carname = carname;
		} else {
			System.arraycopy(carname, 0, this.carname, 0, Math.min(carname.length, this.carname.length));
		}
	}

}
