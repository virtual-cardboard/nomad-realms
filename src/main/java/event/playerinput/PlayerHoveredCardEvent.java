package event.playerinput;

import event.network.p2p.game.CardHoveredNetworkEvent;
import model.id.CardPlayerId;
import model.id.WorldCardId;

public class PlayerHoveredCardEvent extends NomadRealmsPlayerInputEvent {

	private CardPlayerId playerID;
	private WorldCardId cardID;

	public PlayerHoveredCardEvent(CardPlayerId playerID, WorldCardId cardID) {
		this.playerID = playerID;
		this.cardID = cardID;
	}

	public CardHoveredNetworkEvent toNetworkEvent() {
		return new CardHoveredNetworkEvent(playerID.toLongID(), cardID.toLongID());
	}

}
