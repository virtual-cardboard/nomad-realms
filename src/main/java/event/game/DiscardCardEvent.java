package event.game;

import static math.IntegerRandom.randomInt;

import model.GameState;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.CardZone;
import model.card.GameCard;

public class DiscardCardEvent extends CardEffectEvent {

	private static final long serialVersionUID = 6055334094455639128L;

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
	public void process(GameState state) {
		CardDashboard dashboard = state.dashboard(target);
		CardZone hand = dashboard.hand();
		if (hand.empty()) {
			return;
		}
		GameCard card = hand.drawCard(randomInt(hand.size()));
		dashboard.discard().addTop(card);
	}

}
