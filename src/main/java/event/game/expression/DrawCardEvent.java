package event.game.expression;

import model.GameState;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.GameCard;

public class DrawCardEvent extends ChainEvent {

	private static final long serialVersionUID = -2617624732687443704L;

	private int num;
	private CardPlayer target;

	public DrawCardEvent(CardPlayer source, CardPlayer target, int num) {
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
		if (dashboard.deck().empty()) {
			return;
		}
		GameCard card = dashboard.deck().drawTop();
		if (dashboard.hand().full()) {
			dashboard.discard().addTop(card);
		} else {
			dashboard.hand().addTop(card);
		}
	}

}
