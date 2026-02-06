package nomadrealms.user;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.render.ui.Camera;

public class Player {

	private String name;
	private NetworkAddress address;
	private Camera camera;
	private CardPlayer cardPlayer;

	public Player(String name, NetworkAddress address, Camera camera) {
		this.name = name;
		this.address = address;
		this.camera = camera;
	}

	public String name() {
		return name;
	}

	public NetworkAddress address() {
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
