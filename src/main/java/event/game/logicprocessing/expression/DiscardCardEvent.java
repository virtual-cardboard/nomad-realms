package event.game.logicprocessing.expression;

import static math.IntegerRandom.randomInt;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.CardZone;
import model.card.GameCard;
import model.chain.FixedTimeChainEvent;

public class DiscardCardEvent extends FixedTimeChainEvent {

	private int num;
	private CardPlayer target;

	public DiscardCardEvent(CardPlayer source, CardPlayer target, int num) {
		super(source);
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
	public int priority() {
		return 5;
	}

	@Override
	public int processTime() {
		return 5;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardDashboard dashboard = target.cardDashboard();
		CardZone hand = dashboard.hand();
		if (hand.empty()) {
			return;
		}
		GameCard card = hand.remove(randomInt(hand.size()));
		dashboard.discard().addTop(card);
	}

}
