package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import event.game.logicprocessing.chain.ChainEvent;
import event.game.visualssync.CardShuffledSyncEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.GameCard;

public class RegenesisEvent extends ChainEvent {

	public RegenesisEvent(CardPlayer playedBy) {
		super(playedBy);
	}

	public RegenesisEvent(long time, CardPlayer playedBy) {
		super(time, playedBy);
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardDashboard dashboard = player().cardDashboard();
		for (GameCard card : dashboard.discard()) {
			sync.add(new CardShuffledSyncEvent(player(), card));
		}
		dashboard.deck().addAll(dashboard.discard());
		dashboard.discard().clear();
		dashboard.deck().shuffle(0);
	}

	public CardPlayer player() {
		return (CardPlayer) source();
	}

	@Override
	public int priority() {
		return 1;
	}

	@Override
	public int processTime() {
		return 15;
	}

}
