package nomadrealms.game.actor.ai;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.cardplayer.CardPlayer;

public abstract class CardPlayerAI {

	protected CardPlayer self;
	private int thinkingTime;

	public CardPlayerAI(CardPlayer self) {
		this.self = self;
		thinkingTime = resetThinkingTime();
	}

	public final void doUpdate(GameState state) {
		if (thinkingTime > 0) {
			thinkingTime--;
			return;
		}
		thinkingTime = resetThinkingTime();
		update(state);
	}

	public abstract void update(GameState state);

	protected abstract int resetThinkingTime();

	public CardPlayer self() {
		return self;
	}

	public void setSelf(CardPlayer self) {
		this.self = self;
	}

}
