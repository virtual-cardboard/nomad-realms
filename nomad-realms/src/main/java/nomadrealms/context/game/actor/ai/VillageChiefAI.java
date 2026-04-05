package nomadrealms.context.game.actor.ai;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.Tile;

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
		// Prevents updating
		if (!self.cardStack().getCards().isEmpty()) {
			return;
		}

		// Simple AI: Move randomly using MEANDER card
		WorldCard cardToPlay = self.deckCollection().deck1().peek();
		if (cardToPlay == null) {
			return;
		}
		List<Tile> neighbors = asList(
				self.tile().ul(state.world),
				self.tile().um(state.world),
				self.tile().ur(state.world),
				self.tile().dl(state.world),
				self.tile().dm(state.world),
				self.tile().dr(state.world)
		);
		Collections.shuffle(neighbors);
		for (Tile neighbor : neighbors) {
			if (neighbor != null && neighbor.actor() == null) {
				self.addNextPlay(new CardPlayedEvent(cardToPlay, self, neighbor));
				break;
			}
		}
	}

	// Probably change this later
	@Override
	protected int resetThinkingTime() {
		return (int) (Math.random() * 10) + 20;
	}
}
