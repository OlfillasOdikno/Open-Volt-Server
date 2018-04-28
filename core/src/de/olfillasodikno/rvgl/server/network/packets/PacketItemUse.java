package de.olfillasodikno.rvgl.server.network.packets;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.structures.Packet;

public class PacketItemUse implements Packet {

	private int id;
	private int id2;

	private float posX;
	private float posY;
	private float posZ;

	private int item;

	private byte yaw;
	private byte pitch;
	private byte roll;
	private byte unknownRot;

	private int count;

	private int unknown;

	public PacketItemUse() {
	}

	@Override
	public void encode(ByteBuffer buf) {
		buf.putFloat(posX);
		buf.putFloat(posY);
		buf.putFloat(posZ);

		buf.putInt(unknown);

		buf.putInt(item);

		buf.put(pitch);
		buf.put(yaw);
		buf.put(roll);
		buf.put(unknownRot);

		buf.putInt(count);
		buf.putInt(id);
		buf.putInt(id2);
		
		buf.putInt(1);
	}

	@Override
	public void decode(ByteBuffer buf) {
		posX = buf.getFloat();
		posY = buf.getFloat();
		posZ = buf.getFloat();

		unknown = buf.getInt();

		item = buf.getInt();

		pitch = buf.get();
		yaw = buf.get();
		roll = buf.get();
		unknownRot = buf.get();
		
		count = buf.getInt();

		id = buf.getInt();
		id2 = buf.getInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public float getPosZ() {
		return posZ;
	}

	public void setPosZ(float posZ) {
		this.posZ = posZ;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public byte getYaw() {
		return yaw;
	}

	public void setYaw(byte yaw) {
		this.yaw = yaw;
	}

	public byte getPitch() {
		return pitch;
	}

	public void setPitch(byte pitch) {
		this.pitch = pitch;
	}

	public byte getRoll() {
		return roll;
	}

	public void setRoll(byte roll) {
		this.roll = roll;
	}

	public byte getUnknownRot() {
		return unknownRot;
	}

	public void setUnknownRot(byte unknownRot) {
		this.unknownRot = unknownRot;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getUnknown() {
		return unknown;
	}

	public void setUnknown(int unknown) {
		this.unknown = unknown;
	}

}
