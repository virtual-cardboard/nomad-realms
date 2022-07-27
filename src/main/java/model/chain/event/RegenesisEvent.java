package model.chain.event;

import engine.common.QueueGroup;
import event.sync.CardShuffledSyncEvent;
import math.IdGenerators;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.id.CardPlayerId;
import model.state.GameState;

public class RegenesisEvent extends FixedTimeChainEvent {

	public RegenesisEvent(CardPlayerId playerID) {
		super(playerID);
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, QueueGroup queueGroup) {
		CardPlayer cardPlayer = playerID().getFrom(state);
		CardDashboard dashboard = cardPlayer.cardDashboard();
		for (WorldCard card : dashboard.discard()) {
			queueGroup.pushEventFromLogic(new CardShuffledSyncEvent(playerID(), card.id()));
		}
		dashboard.deck().addAll(dashboard.discard());
		dashboard.discard().clear();
		dashboard.deck().shuffle(cardPlayer.random(tick));
	}

	@Override
	public int priority() {
		return 20;
	}

	@Override
	public int processTime() {
		return 15;
	}

	@Override
	public String textureName() {
		return "regenesis";
	}

}
