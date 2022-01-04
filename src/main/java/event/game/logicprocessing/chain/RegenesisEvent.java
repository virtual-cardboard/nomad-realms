package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.CardShuffledSyncEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.state.GameState;

public class RegenesisEvent extends FixedTimeChainEvent {

	public RegenesisEvent(long playerID) {
		super(playerID);
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardPlayer cardPlayer = state.cardPlayer(playerID());
		CardDashboard dashboard = cardPlayer.cardDashboard();
		for (WorldCard card : dashboard.discard()) {
			sync.add(new CardShuffledSyncEvent(playerID(), card.id()));
		}
		dashboard.deck().addAll(dashboard.discard());
		dashboard.discard().clear();
		dashboard.deck().shuffle(0);
	}

	@Override
	public int priority() {
		return 20;
	}

	@Override
	public int processTime() {
		return 15;
	}

}
