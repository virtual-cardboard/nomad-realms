package context.game.logic;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.expression.DrawCardEvent;
import event.game.visualssync.CardDrawnSyncEvent;
import event.game.visualssync.CardMilledSyncEvent;
import model.card.CardDashboard;
import model.card.GameCard;

public class DrawCardEventHandler implements Consumer<DrawCardEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> sync;

	public DrawCardEventHandler(NomadsGameData data, Queue<GameEvent> sync) {
		this.data = data;
		this.sync = sync;
	}

	@Override
	public void accept(DrawCardEvent t) {
		CardDashboard dashboard = data.state().dashboard(t.target());
		for (int i = 0; i < t.num(); i++) {
			if (dashboard.deck().empty()) {
				return;
			}
			GameCard card = dashboard.deck().drawTop();
			if (dashboard.hand().full()) {
				sync.add(new CardMilledSyncEvent(t.target(), card));
				dashboard.discard().addTop(card);
			} else {
				sync.add(new CardDrawnSyncEvent(t.target(), card));
				dashboard.hand().addTop(card);
			}
		}
	}

}
