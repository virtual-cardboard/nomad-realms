package event.game.logicprocessing.expression;

import model.actor.CardPlayer;

public class DiscardCardEvent extends CardExpressionEvent {

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

//	@Override
//	public void process(GameState state) {
//		CardDashboard dashboard = state.dashboard(target);
//		CardZone hand = dashboard.hand();
//		if (hand.empty()) {
//			return;
//		}
//		GameCard card = hand.drawCard(randomInt(hand.size()));
//		dashboard.discard().addTop(card);
//	}

}
