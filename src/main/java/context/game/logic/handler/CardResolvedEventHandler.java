package context.game.logic.handler;

import static model.card.CardType.TASK;

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
		GameState nextState = data.nextState();
		CardPlayerId playerID = t.playerID();
		Id targetId = t.targetID();
		WorldCardId cardID = t.cardID();
		CardPlayer player = playerID.getFrom(nextState);
		WorldCard card = cardID.getFrom(nextState);
		if (card.type() == TASK) {
			player.cardDashboard().cancelTask();
		}
		EffectChain chain = card.effect().resolutionChain(playerID, targetId, nextState, data.generators());
		// TODO notify observers for "whenever" effects
		// TODO notify observers for "after" effects

		nextState.chainHeap().add(chain);
	}

}
