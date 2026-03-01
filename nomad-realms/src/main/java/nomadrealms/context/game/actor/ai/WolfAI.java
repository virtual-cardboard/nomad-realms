package nomadrealms.context.game.actor.ai;

import static java.util.Comparator.comparingInt;
import static nomadrealms.context.game.card.GameCard.MEANDER;
import static nomadrealms.context.game.card.GameCard.MELEE_ATTACK;

import java.util.Optional;
import java.util.stream.Stream;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.Wolf;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.condition.RangeCondition;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.Tile;

public class WolfAI extends CardPlayerAI {

	/**
	 * No-arg constructor for serialization.
	 */
	protected WolfAI() {
	}

	public WolfAI(CardPlayer self) {
		super(self);
	}

	/**
	 * Wolf AI decision-making.
	 * <p>
	 * Finds the nearest non-Wolf actor within 20 tiles, and either moves towards it or attacks it, depending on
	 * the distance.
	 *
	 * @param state the current game state
	 */
	@Override
	public void update(GameState state) {
		if (!self.cardStack().getCards().isEmpty()) {
			return;
		}
		// Find the nearest actor that is within 20 tiles, and is NOT a wolf
		CardPlayer nearestCardPlayer = state.world.actors.stream()
				.filter(actor -> !(actor instanceof Wolf))
				.filter(actor -> actor instanceof CardPlayer)
				.map(actor -> (CardPlayer) actor)
				.filter(actor -> !actor.isDestroyed())
				.filter(actor -> actor.tile().coord().distanceTo(self.tile().coord()) < 20)
				.min(comparingInt(a -> a.tile().coord().distanceTo(self.tile().coord())))
				.orElse(null);
		if (nearestCardPlayer == null) {
			return;
		}

		// If there is an actor, go towards it if it's out of range
		// If it's within range, attack it
		int range = MELEE_ATTACK.targetingInfo().conditions().stream()
				.filter(c -> c instanceof RangeCondition)
				.map(c -> (RangeCondition) c)
				.mapToInt(RangeCondition::distance)
				.findFirst()
				.orElse(0);
		if (nearestCardPlayer.tile().coord().distanceTo(self.tile().coord()) > range) {
			// For each 6 directions, check if the tile in that direction is closer to the target
			Optional<Tile> closestTile = Stream
					.of(
							self.tile().dl(state.world),
							self.tile().dm(state.world),
							self.tile().dr(state.world),
							self.tile().ul(state.world),
							self.tile().um(state.world),
							self.tile().ur(state.world)
					)
					.filter(tile -> tile.isWalkable(self))
					.min(comparingInt(t -> t.coord().distanceTo(nearestCardPlayer.tile().coord())));
			if (!closestTile.isPresent()) {
				return;
			}
			WorldCard cardToPlay = self.deckCollection().deck1().peek();
			if (cardToPlay != null) {
				assert cardToPlay.card() == MEANDER;
				self.addNextPlay(new CardPlayedEvent(cardToPlay, self, closestTile.get()));
			}
		} else {
			WorldCard cardToPlay = self.deckCollection().deck2().peek();
			if (cardToPlay != null) {
				assert cardToPlay.card() == MELEE_ATTACK;
				self.addNextPlay(new CardPlayedEvent(cardToPlay, self, nearestCardPlayer));
			}
		}
	}

	@Override
	protected int resetThinkingTime() {
		return (int) (Math.random() * 2) + 10;
	}

}
