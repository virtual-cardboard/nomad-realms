package context.game.logic;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.visualssync.CardResolvedSyncEvent;

public class CardResolvedEventHandler implements Consumer<CardResolvedEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> sync;

	public CardResolvedEventHandler(NomadsGameData data, Queue<GameEvent> sync) {
		this.data = data;
		this.sync = sync;
	}

	@Override
	public void accept(CardResolvedEvent t) {
		data.state().chainHeap().add(t.card().effect().resolutionChain(t.player(), t.target(), data.state()));
		data.state().dashboard(t.player()).discard().addTop(t.card());
		sync.add(new CardResolvedSyncEvent(t.player(), t.card()));
	}

}
