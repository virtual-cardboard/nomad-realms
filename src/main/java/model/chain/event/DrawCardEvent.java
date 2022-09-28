package model.chain.event;

import engine.common.ContextQueues;
import event.sync.CardDrawnSyncEvent;
import event.sync.CardMilledSyncEvent;
import math.IdGenerators;
import model.actor.health.cardplayer.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.id.CardPlayerId;
import model.state.GameState;

public class DrawCardEvent extends FixedTimeChainEvent {

	private CardPlayerId targetID;
	private int amount;

	public DrawCardEvent(CardPlayerId playerID, CardPlayerId targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, ContextQueues contextQueues) {
		CardPlayer target = targetID.getFrom(state);
		CardDashboard dashboard = target.cardDashboard();
		for (int i = 0; i < amount; i++) {
			if (dashboard.deck().empty()) {
				return;
			}
			WorldCard card = dashboard.deck().drawTop();
			if (dashboard.hand().full()) {
				contextQueues.pushEventFromLogic(new CardMilledSyncEvent(playerID(), playerID(), card.id()));
				dashboard.discard().addTop(card);
			} else {
				contextQueues.pushEventFromLogic(new CardDrawnSyncEvent(playerID(), playerID(), card.id()));
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
		return 25;
	}

	@Override
	public boolean cancelled(GameState state) {
		return super.cancelled(state) || targetID.getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return "draw_card";
	}

	public CardPlayerId targetID() {
		return targetID;
	}

}
