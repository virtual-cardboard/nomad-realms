package event.game.playerinput;

import event.network.game.CardHoveredNetworkEvent;
import model.actor.CardPlayer;
import model.id.ID;
import model.id.WorldCardID;

public class PlayerHoveredCardEvent extends NomadRealmsPlayerInputEvent {

	private ID<? extends CardPlayer> playerID;
	private WorldCardID cardID;

	public PlayerHoveredCardEvent(ID<? extends CardPlayer> playerID, WorldCardID cardID) {
		this.playerID = playerID;
		this.cardID = cardID;
	}

	public CardHoveredNetworkEvent toNetworkEvent() {
		return new CardHoveredNetworkEvent(time(), playerID.toLongID(), cardID.toLongID());
	}

}
