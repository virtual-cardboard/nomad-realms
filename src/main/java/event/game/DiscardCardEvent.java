package event.game;

import model.GameState;
import model.actor.CardPlayer;

public class DiscardCardEvent extends CardEffectEvent {

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

	}

}
