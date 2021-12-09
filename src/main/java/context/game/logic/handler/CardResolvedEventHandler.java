package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.logicprocessing.chain.UnlockQueueChainEvent;
import event.game.visualssync.CardResolvedSyncEvent;
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
		EffectChain chain = t.card().effect().resolutionChain(t.player(), t.target(), data.state());
		// TODO notify observers for "whenever" effects
		chain.add(new UnlockQueueChainEvent(t.player()));
		// TODO notify observers for "after" effects
		t.player().cardDashboard().discard().addTop(t.card());
		data.state().chainHeap().add(chain);
		networkSync.add(new CardResolvedSyncEvent(t.player(), t.card()));
		visualSync.add(new CardResolvedSyncEvent(t.player(), t.card()));
	}

}
