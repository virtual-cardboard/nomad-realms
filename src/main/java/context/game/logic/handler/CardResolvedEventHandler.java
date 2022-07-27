package context.game.logic.handler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.logicprocessing.CardResolvedEvent;
import model.actor.CardPlayer;
import model.card.WorldCard;
import model.chain.EffectChain;
import model.id.CardPlayerId;
import model.id.Id;
import model.id.WorldCardId;
import model.state.GameState;

public class CardResolvedEventHandler implements Consumer<CardResolvedEvent> {

	private final NomadsGameData data;

	public CardResolvedEventHandler(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public void accept(CardResolvedEvent t) {
		GameState currentState = data.currentState();
		CardPlayerId playerID = t.playerID();
		Id targetId = t.targetID();
		WorldCardId cardID = t.cardID();
		CardPlayer player = playerID.getFrom(currentState);
		WorldCard card = cardID.getFrom(currentState);
		player.cardDashboard().discard().addTop(card);
		EffectChain chain = card.effect().resolutionChain(playerID, targetId, currentState, data.generators());
		// TODO notify observers for "whenever" effects
		// TODO notify observers for "after" effects

		currentState.chainHeap().add(chain);
	}

}
