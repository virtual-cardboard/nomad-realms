package nomadrealms.game.actor.ai;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.cardplayer.CardPlayer;

public abstract class CardPlayerAI {

	private final CardPlayer cardPlayer;
	private int thinkingTime;

	// Constructor
	public CardPlayerAI(CardPlayer cardPlayer) {
		this.cardPlayer = cardPlayer;
	}

	public final void doUpdate(CardPlayer cardPlayer, GameState state) {
		if (thinkingTime > 0) {
			thinkingTime--;
			return;
		}
		thinkingTime = resetThinkingTime();
		update(cardPlayer, state);
	}

	public abstract void update(CardPlayer cardPlayer, GameState state);

	protected abstract int resetThinkingTime();

	protected CardPlayer cardPlayer() {
		return cardPlayer;
	}

}
