package nomadrealms.user;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;

public class Player {

	private String name;
	private PacketAddress address;
	private CardPlayer cardPlayer;

	public Player(String name, PacketAddress address) {
		this.name = name;
		this.address = address;
	}

	public String name() {
		return name;
	}

	public PacketAddress address() {
		return address;
	}

	public CardPlayer cardPlayer() {
		return cardPlayer;
	}

	public Player cardPlayer(CardPlayer cardPlayer) {
		this.cardPlayer = cardPlayer;
		return this;
	}

}
