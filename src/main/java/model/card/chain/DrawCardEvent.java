package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.CardDrawnSyncEvent;
import event.game.visualssync.CardMilledSyncEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.id.ID;
import model.state.GameState;

public class DrawCardEvent extends FixedTimeChainEvent {

	private ID<? extends CardPlayer> targetID;
	private int amount;

	public DrawCardEvent(ID<? extends CardPlayer> playerID, ID<? extends CardPlayer> targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardPlayer target = targetID.getFrom(state);
		CardDashboard dashboard = target.cardDashboard();
		for (int i = 0; i < amount; i++) {
			if (dashboard.deck().empty()) {
				return;
			}
			WorldCard card = dashboard.deck().drawTop();
			if (dashboard.hand().full()) {
				sync.add(new CardMilledSyncEvent(playerID(), playerID(), card.id()));
				dashboard.discard().addTop(card);
			} else {
				sync.add(new CardDrawnSyncEvent(playerID(), playerID(), card.id()));
				dashboard.hand().addTop(card);
			}
		}
	}

	@Override
	public int priority() {
		return 7;
	}

	@Override
	public int processTime() {
		return 5;
	}

	@Override
	public boolean cancelled(GameState state) {
		return super.cancelled(state) || targetID.getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return "draw_card";
	}

	public ID<? extends CardPlayer> targetID() {
		return targetID;
	}

}
