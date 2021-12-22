package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.CardDrawnSyncEvent;
import event.game.visualssync.CardMilledSyncEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.GameCard;
import model.chain.FixedTimeChainEvent;

public class DrawCardEvent extends FixedTimeChainEvent {

	private int num;
	private CardPlayer target;

	public DrawCardEvent(CardPlayer player, CardPlayer target, int num) {
		super(player);
		this.target = target;
		this.num = num;
	}

	public int num() {
		return num;
	}

	public CardPlayer target() {
		return target;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardDashboard dashboard = target.cardDashboard();
		for (int i = 0; i < num; i++) {
			if (dashboard.deck().empty()) {
				return;
			}
			GameCard card = dashboard.deck().drawTop();
			if (dashboard.hand().full()) {
				sync.add(new CardMilledSyncEvent(player(), target, card));
				dashboard.discard().addTop(card);
			} else {
				sync.add(new CardDrawnSyncEvent(player(), target, card));
				dashboard.hand().addTop(card);
			}
		}
	}

	@Override
	public int priority() {
		return 6;
	}

	@Override
	public int processTime() {
		return 5;
	}

	@Override
	public boolean cancelled() {
		return super.cancelled() || target.isDead();
	}

}
