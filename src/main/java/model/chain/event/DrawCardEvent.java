package model.chain.event;

import common.QueueGroup;
import event.game.sync.CardDrawnSyncEvent;
import event.game.sync.CardMilledSyncEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.id.CardPlayerID;
import model.state.GameState;

public class DrawCardEvent extends FixedTimeChainEvent {

	private CardPlayerID targetID;
	private int amount;

	public DrawCardEvent(CardPlayerID playerID, CardPlayerID targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	@Override
	public void process(long tick, GameState state, QueueGroup queueGroup) {
		CardPlayer target = targetID.getFrom(state);
		CardDashboard dashboard = target.cardDashboard();
		for (int i = 0; i < amount; i++) {
			if (dashboard.deck().empty()) {
				return;
			}
			WorldCard card = dashboard.deck().drawTop();
			if (dashboard.hand().full()) {
				queueGroup.pushEventFromLogic(new CardMilledSyncEvent(playerID(), playerID(), card.id()));
				dashboard.discard().addTop(card);
			} else {
				queueGroup.pushEventFromLogic(new CardDrawnSyncEvent(playerID(), playerID(), card.id()));
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

	public CardPlayerID targetID() {
		return targetID;
	}

}
