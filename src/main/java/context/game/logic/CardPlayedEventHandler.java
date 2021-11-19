
package context.game.logic;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import model.card.CardDashboard;
import model.card.CardQueue;
import model.card.CardType;
import model.card.GameCard;
import model.card.effect.CardEffect;

public class CardPlayedEventHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> sync;

	public CardPlayedEventHandler(NomadsGameData data, Queue<GameEvent> sync) {
		this.data = data;
		this.sync = sync;
	}

	@Override
	public void accept(CardPlayedEvent event) {
		CardDashboard dashboard = data.state().dashboard(data.player());
		GameCard card = event.card();
		int index = dashboard.hand().indexOf(card.id());
		dashboard.hand().delete(index);
		if (card.type() == CardType.CANTRIP) {
			dashboard.discard().addTop(card);
			playCantrip(event);
		} else {
			CardQueue queue = dashboard.queue();
			queue.append(event);
		}
		sync.add(event);
	}

	private void playCantrip(CardPlayedEvent cpe) {
		System.out.println("Played card " + cpe.card().name());
		CardEffect effect = cpe.card().effect();
		if (effect.expression != null) {
			data.state().chainHeap().add(cpe.card().effect().resolutionChain(cpe.player(), cpe.target(), data.state()));
			System.out.println("Triggered effect!");
		} else {
			System.out.println("Null effect");
		}
	}

}
