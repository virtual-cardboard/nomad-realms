package event.network;

import static context.data.user.LocalUser.LOCAL_USER;

import java.io.Serializable;

public class CardPlayedNetworkEvent extends NomadRealmsNetworkEvent implements Serializable {

	private static final long serialVersionUID = -4672057528230548804L;

	private long player;
	private long card;
	private long target;

	public CardPlayedNetworkEvent(long time, long player, long target, long card) {
		super(time, LOCAL_USER);
		this.player = player;
		this.card = card;
		this.target = target;
	}

	/**
	 * 
	 * @param time
	 * @param player id of the card player
	 * @param card   id of the card
	 * @param target id of the target
	 */
	public CardPlayedNetworkEvent(long player, long target, long card) {
		super(LOCAL_USER);
		this.player = player;
		this.card = card;
		this.target = target;
	}

	public long player() {
		return player;
	}

	public long card() {
		return card;
	}

	public long target() {
		return target;
	}

}
