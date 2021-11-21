
package context.game.logic;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.visualssync.CardPlayedSyncEvent;
import event.network.CardPlayedNetworkEvent;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.CardType;
import model.card.GameCard;
import model.card.effect.CardEffect;

public class CardPlayedEventHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> networkSync;
	private Queue<GameEvent> visualSync;

	public CardPlayedEventHandler(NomadsGameData data, Queue<GameEvent> networkSync, Queue<GameEvent> visualSync) {
		this.data = data;
		this.networkSync = networkSync;
		this.visualSync = visualSync;
	}

	@Override
	public void accept(CardPlayedEvent event) {
		CardDashboard dashboard = data.state().dashboard(event.player());
		GameCard card = event.card();
		int index = dashboard.hand().indexOf(card.id());
		dashboard.hand().remove(index);
		if (card.type() == CardType.CANTRIP) {
			dashboard.discard().addTop(card);
			playCantrip(event);
		} else {
			CardQueue queue = dashboard.queue();
			queue.append(event);
		}
		if (event.player() == data.player()) {
			networkSync.add(new CardPlayedNetworkEvent(event.player().id(), event.target().id(), card.id()));
		}
		visualSync.add(new CardPlayedSyncEvent(event.player(), card));
	}

	private void playCantrip(CardPlayedEvent cpe) {
		CardEffect effect = cpe.card().effect();
		if (effect.expression != null) {
			data.state().chainHeap().add(cpe.card().effect().resolutionChain(cpe.player(), cpe.target(), data.state()));
		}
	}

}
