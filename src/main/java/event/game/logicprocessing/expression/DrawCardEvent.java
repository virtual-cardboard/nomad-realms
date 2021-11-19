package event.game.logicprocessing.expression;

import model.actor.CardPlayer;

public class DrawCardEvent extends CardExpressionEvent {

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

}
