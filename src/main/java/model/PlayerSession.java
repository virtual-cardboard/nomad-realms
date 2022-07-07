package model;

import static model.ModelSerializationFormats.PLAYER_SESSION;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.Serializable;
import engine.common.networking.packet.address.PacketAddress;

public class PlayerSession implements Serializable {

	private Player player;
	private PacketAddress lanAddress;
	private PacketAddress wanAddress;

	public PlayerSession() {
	}

	public PlayerSession(Player player, PacketAddress lanAddress, PacketAddress wanAddress) {
		this.player = player;
		this.lanAddress = lanAddress;
		this.wanAddress = wanAddress;
	}

	public PlayerSession(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public ModelSerializationFormats formatEnum() {
		return PLAYER_SESSION;
	}

	@Override
	public void read(SerializationReader reader) {
		this.player = new Player();
		this.player.read(reader);
		this.lanAddress = new PacketAddress();
		this.lanAddress.read(reader);
		this.wanAddress = new PacketAddress();
		this.wanAddress.read(reader);
	}

	@Override
	public void write(SerializationWriter writer) {
		player.write(writer);
		lanAddress.write(writer);
		wanAddress.write(writer);
	}

	public Player player() {
		return player;
	}

	public PacketAddress lanAddress() {
		return lanAddress;
	}

	public PacketAddress wanAddress() {
		return wanAddress;
	}

}
