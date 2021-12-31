package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.logicprocessing.chain.UnlockQueueChainEvent;
import event.game.visualssync.CardResolvedSyncEvent;
import model.actor.CardPlayer;
import model.card.GameCard;
import model.chain.ChainEndEvent;
import model.chain.EffectChain;

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
		long playerID = t.playerID();
		long targetID = t.targetID();
		CardPlayer player = data.nextState().cardPlayer(playerID);
		GameCard card = data.nextState().card(t.cardID());
		EffectChain chain = card.effect().resolutionChain(playerID, targetID, data.nextState());
		// TODO notify observers for "whenever" effects
		chain.add(new UnlockQueueChainEvent(playerID));
		// TODO notify observers for "after" effects
		chain.add(new ChainEndEvent(playerID, chain));

		player.cardDashboard().discard().addTop(card);
		player.addChain(chain);
		data.nextState().chainHeap().add(chain);
		networkSync.add(new CardResolvedSyncEvent(player, card));
		visualSync.add(new CardResolvedSyncEvent(player, card));
	}

}
