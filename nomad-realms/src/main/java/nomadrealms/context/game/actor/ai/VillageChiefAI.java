package nomadrealms.context.game.actor.ai;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;

public class VillageChiefAI extends CardPlayerAI {

	/**
	 * No-arg constructor for serialization.
	 */
	protected VillageChiefAI() {
	}

	public VillageChiefAI(CardPlayer self) {
		super(self);
	}

	/**
	 * Village Chief AI decision-making.
	 *
	 * @param state the current game state
	 */
	@Override
	public void update(GameState state) {
		// Simple AI: Move randomly using MEANDER card
		WorldCard cardToPlay = self.deckCollection().deck1().peek();
		self.addNextPlay(new CardPlayedEvent(cardToPlay, self, self.tile().dl((state.world))));
	}

	// Probably change this later
	@Override
	protected int resetThinkingTime() {
		return 0;
	}
}
