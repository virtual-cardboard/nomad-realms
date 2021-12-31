package event.game.playerinput;

import event.network.game.CardHoveredNetworkEvent;

public class PlayerHoveredCardEvent extends NomadRealmsPlayerInputEvent {

	private long playerID;
	private long cardID;

	public PlayerHoveredCardEvent(long playerID, long cardID) {
		this.playerID = playerID;
		this.cardID = cardID;
	}

	public CardHoveredNetworkEvent toNetworkEvent() {
		return new CardHoveredNetworkEvent(time(), playerID, cardID);
	}

}
