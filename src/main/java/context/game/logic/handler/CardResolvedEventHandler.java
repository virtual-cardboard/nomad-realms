package context.game.logic.handler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardResolvedEvent;
import model.actor.CardPlayer;
import model.card.WorldCard;
import model.chain.EffectChain;
import model.id.CardPlayerID;
import model.id.ID;
import model.id.WorldCardID;
import model.state.GameState;

public class CardResolvedEventHandler implements Consumer<CardResolvedEvent> {

	private NomadsGameData data;

	public CardResolvedEventHandler(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public void accept(CardResolvedEvent t) {
		GameState currentState = data.currentState();
		CardPlayerID playerID = t.playerID();
		ID targetID = t.targetID();
		WorldCardID cardID = t.cardID();
		CardPlayer player = playerID.getFrom(currentState);
		WorldCard card = cardID.getFrom(currentState);
		player.cardDashboard().discard().addTop(card);
		EffectChain chain = card.effect().resolutionChain(playerID, targetID, currentState);
		// TODO notify observers for "whenever" effects
		// TODO notify observers for "after" effects

		currentState.chainHeap().add(chain);
	}

}
