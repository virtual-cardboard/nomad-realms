package nomadrealms.context.game.actor.ai;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;

public abstract class CardPlayerAI {

	protected transient CardPlayer self;
	private int thinkingTime;

	/**
	 * No-arg constructor for serialization.
	 */
	protected CardPlayerAI() {
	}

	public CardPlayerAI(CardPlayer self) {
		this.self = self;
		thinkingTime = resetThinkingTime();
	}

	public final void doUpdate(GameState state) {
		if (thinkingTime > 0) {
			thinkingTime--;
			return;
		}
		update(state);
	}

	public final void resetThinkingTimer() {
		this.thinkingTime = resetThinkingTime();
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
