package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.visualssync.CardResolvedSyncEvent;
import model.chain.ChainEndEvent;
import model.chain.EffectChain;
import model.chain.UnlockQueueChainEvent;

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
		EffectChain chain = t.card().effect().resolutionChain(t.player(), t.target(), data.nextState());
		// TODO notify observers for "whenever" effects
		chain.add(new UnlockQueueChainEvent(t.player()));
		// TODO notify observers for "after" effects
		chain.add(new ChainEndEvent(t.player(), chain));

		t.player().cardDashboard().discard().addTop(t.card());
		t.player().addChain(chain);
		data.nextState().chainHeap().add(chain);
		networkSync.add(new CardResolvedSyncEvent(t.player(), t.card()));
		visualSync.add(new CardResolvedSyncEvent(t.player(), t.card()));
	}

}
