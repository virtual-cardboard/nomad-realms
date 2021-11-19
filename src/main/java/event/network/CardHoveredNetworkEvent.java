package event.network;

import static context.data.user.LocalUser.LOCAL_USER;

import java.io.Serializable;

public class CardHoveredNetworkEvent extends NomadRealmsNetworkEvent implements Serializable {

	private static final long serialVersionUID = -4672057528230548804L;

	private long player;
	private long card;

	public CardHoveredNetworkEvent(long time, long player, long card) {
		super(time, LOCAL_USER);
		this.player = player;
		this.card = card;
	}

	public long player() {
		return player;
	}

	public long card() {
		return card;
	}

}
