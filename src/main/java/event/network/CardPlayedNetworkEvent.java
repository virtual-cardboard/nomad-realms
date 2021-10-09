package event.network;

import common.event.NetworkEvent;
import context.data.user.LocalUser;

public class CardPlayedNetworkEvent extends NetworkEvent {

	private long playedBy;
	private long card;
	private long target;

	/**
	 * 
	 * @param time
	 * @param playedBy id of the card player
	 * @param card     id of the card
	 * @param target   id of the target
	 */
	public CardPlayedNetworkEvent(long time, long playedBy, long card, long target) {
		super(time, LocalUser.LOCAL_USER);
		this.playedBy = playedBy;
		this.card = card;
		this.target = target;
	}

	public long playedBy() {
		return playedBy;
	}

	public long card() {
		return card;
	}

	public long target() {
		return target;
	}

}
