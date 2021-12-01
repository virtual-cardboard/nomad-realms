package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.visualssync.CardResolvedSyncEvent;

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
		data.state().chainHeap().add(t.card().effect().resolutionChain(t.player(), t.target(), data.state()));
		data.state().dashboard(t.player()).discard().addTop(t.card());
		networkSync.add(new CardResolvedSyncEvent(t.player(), t.card()));
		visualSync.add(new CardResolvedSyncEvent(t.player(), t.card()));
	}

}
