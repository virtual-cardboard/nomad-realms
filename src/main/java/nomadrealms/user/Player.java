package nomadrealms.user;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.render.ui.Camera;

public class Player {

	private String name;
	private PacketAddress address;
	private Camera camera;
	private CardPlayer cardPlayer;

	public Player(String name, PacketAddress address, Camera camera) {
		this.name = name;
		this.address = address;
		this.camera = camera;
	}

	public String name() {
		return name;
	}

	public PacketAddress address() {
		return address;
	}

	public Camera camera() {
		return camera;
	}

	public CardPlayer cardPlayer() {
		return cardPlayer;
	}

	public void cardPlayer(CardPlayer cardPlayer) {
		this.cardPlayer = cardPlayer;
	}

}
