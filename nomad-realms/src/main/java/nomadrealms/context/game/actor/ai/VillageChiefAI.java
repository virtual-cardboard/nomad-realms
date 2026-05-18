package nomadrealms.context.game.actor.ai;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.stream.Stream;

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
		if (!self.cardStack().getCards().isEmpty()) {
			return;
		}

		// Simple AI: Move randomly using MEANDER card
		WorldCard cardToPlay = self.deckCollection().deck1().peek();
		if (cardToPlay == null) {
			return;
		}
		List<Tile> neighbors = Stream.of(
						self.tile().dl(state.world),
						self.tile().dm(state.world),
						self.tile().dr(state.world),
						self.tile().ul(state.world),
						self.tile().um(state.world),
						self.tile().ur(state.world)
				)
				.filter(tile -> tile != null && tile.actor() == null)
				.toList();
		if (!neighbors.isEmpty()) {
			Tile targetTile = neighbors.get(self.tile().chunk().zone().rng().nextInt(neighbors.size()));
			self.addNextPlay(new CardPlayedEvent(cardToPlay, self, targetTile));
		}
	}

	// Probably change this later
	@Override
	protected int resetThinkingTime() {
		return self.tile().chunk().zone().rng().nextInt(2) + 10;
	}
}
