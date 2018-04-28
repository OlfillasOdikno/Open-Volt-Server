package de.olfillasodikno.rvgl.server.structures.ingame;

import java.nio.ByteBuffer;

import de.olfillasodikno.rvgl.server.Constants;
import de.olfillasodikno.rvgl.server.structures.Player;

public class IngamePlayer extends IngameObject{

	private float posX;
	private float posY;
	private float posZ;

	private byte yaw;
	private byte pitch;
	private byte roll;
	private byte unknownRot;

	private byte vk_up_down;
	private byte vk_left_right;
	private byte vk_special;

	private int count;

	private int id;

	private byte[] unknown;
	public IngamePlayer() {}
	public IngamePlayer(Player p) {
		unknown = new byte[12];
		id = p.getId();
	}

	@Override
	public void encodeBody(ByteBuffer buf) {
		buf.putFloat(posX);
		buf.putFloat(posY);
		buf.putFloat(posZ);

		buf.put(unknown);

		buf.put(pitch);
		buf.put(yaw);
		buf.put(roll);
		buf.put(unknownRot);

		buf.put(vk_left_right);
		buf.put(vk_up_down);
		buf.put(vk_special);
		buf.put((byte) 0);

		buf.putInt(count);

		buf.putInt(id);

		buf.putInt(1);
	}
	
	@Override
	public void decodeBody(ByteBuffer buf) {

		posX = buf.getFloat();
		posY = buf.getFloat();
		posZ = buf.getFloat();
		
		if(Constants.DEV) {
			System.out.println("X: "+posX+ "	Y: "+posY+"	Z: "+posZ);	
		}

		unknown = new byte[12];
		buf.get(unknown);

		pitch = buf.get();
		yaw = buf.get();
		roll = buf.get();
		unknownRot = buf.get();
		
		vk_left_right = buf.get();
		vk_up_down = buf.get();
		vk_special = buf.get();
		buf.get();

		count = buf.getInt();
		
		id = buf.getInt();

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

	public byte getVk_up_down() {
		return vk_up_down;
	}

	public void setVk_up_down(byte vk_up_down) {
		this.vk_up_down = vk_up_down;
	}

	public byte getVk_left_right() {
		return vk_left_right;
	}

	public void setVk_left_right(byte vk_left_right) {
		this.vk_left_right = vk_left_right;
	}

	public byte getVk_special() {
		return vk_special;
	}

	public void setVk_special(byte vk_special) {
		this.vk_special = vk_special;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getUnknown() {
		return unknown;
	}

	public void setUnknown(byte[] unknown) {
		this.unknown = unknown;
	}
	
	
}
