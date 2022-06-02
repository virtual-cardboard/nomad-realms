package event.game.playerinput;

import event.network.p2p.game.CardHoveredNetworkEvent;
import model.id.CardPlayerID;
import model.id.WorldCardID;

public class PlayerHoveredCardEvent extends NomadRealmsPlayerInputEvent {

	private CardPlayerID playerID;
	private WorldCardID cardID;

	public PlayerHoveredCardEvent(CardPlayerID playerID, WorldCardID cardID) {
		this.playerID = playerID;
		this.cardID = cardID;
	}

	public CardHoveredNetworkEvent toNetworkEvent() {
		return new CardHoveredNetworkEvent(playerID.toLongID(), cardID.toLongID());
	}

}
