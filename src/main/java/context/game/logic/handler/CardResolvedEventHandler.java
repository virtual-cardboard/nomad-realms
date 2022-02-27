package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.visualssync.CardResolvedSyncEvent;
import model.actor.CardPlayer;
import model.card.WorldCard;
import model.chain.EffectChain;
import model.id.CardPlayerID;
import model.id.ID;
import model.id.WorldCardID;

public class CardResolvedEventHandler implements Consumer<CardResolvedEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> networkSync;
	private Queue<GameEvent> visualSync;

	public CardResolvedEventHandler(NomadsGameData data, Queue<GameEvent> networkSync, Queue<GameEvent> visualSync) {
		this.data = data;
		this.networkSync = networkSync;
		this.visualSync = visualSync;
	}

	@Override
	public void accept(CardResolvedEvent t) {
		CardPlayerID playerID = t.playerID();
		ID targetID = t.targetID();
		WorldCardID cardID = t.cardID();
		CardPlayer player = playerID.getFrom(data.nextState());
		WorldCard card = cardID.getFrom(data.nextState());
		EffectChain chain = card.effect().resolutionChain(playerID, targetID, data.nextState());
		// TODO notify observers for "whenever" effects
		// TODO notify observers for "after" effects

		player.cardDashboard().discard().addTop(card);

		data.nextState().chainHeap().add(chain);

		networkSync.add(new CardResolvedSyncEvent(playerID, cardID));
		visualSync.add(new CardResolvedSyncEvent(playerID, cardID));
	}

}
