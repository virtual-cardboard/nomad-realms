package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.sync.CardShuffledSyncEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.id.CardPlayerID;
import model.state.GameState;

public class RegenesisEvent extends FixedTimeChainEvent {

	public RegenesisEvent(CardPlayerID playerID) {
		super(playerID);
	}

	@Override
	public void process(long tick, GameState state, Queue<GameEvent> sync) {
		CardPlayer cardPlayer = playerID().getFrom(state);
		CardDashboard dashboard = cardPlayer.cardDashboard();
		for (WorldCard card : dashboard.discard()) {
			sync.add(new CardShuffledSyncEvent(playerID(), card.id()));
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
