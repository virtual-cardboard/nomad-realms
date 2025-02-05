package nomadrealms.game.actor.ai;

import static nomadrealms.game.card.GameCard.MELEE_ATTACK;
import static nomadrealms.game.card.GameCard.MEANDER;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.actor.cardplayer.FeralMonkey;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.world.map.area.Tile;

public class FeralMonkeyAI extends CardPlayerAI {

	/**
	 * No-arg constructor for serialization.
	 */
	protected FeralMonkeyAI() {
	}

	public FeralMonkeyAI(CardPlayer self) {
		super(self);
	}

	@Override
	public void update(GameState state) {
		// Find the nearest actor that is within 20 tiles, and is NOT a feral monkey
		CardPlayer nearestCardPlayer = state.world.actors.stream()
				.filter(actor -> !(actor instanceof FeralMonkey))
				.filter(actor -> actor instanceof CardPlayer)
				.map(actor -> (CardPlayer) actor)
				.filter(actor -> actor.tile().coord().distanceTo(self.tile().coord()) < 20)
				.min(Comparator.comparingInt(a -> a.tile().coord().distanceTo(self.tile().coord())))
				.orElse(null);
		if (nearestCardPlayer == null) {
			return;
		}

		// If there is an actor, go towards it if it's out of range
		// If it's within range, attack it
		if (nearestCardPlayer.tile().coord().distanceTo(self.tile().coord()) > MELEE_ATTACK.targetingInfo().range()) {
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
					.filter(tile -> state.world.tileToEntityMap.get(tile) == null)
					.min(Comparator.comparingInt(t -> t.coord().distanceTo(nearestCardPlayer.tile().coord())));
			if (!closestTile.isPresent()) {
				return;
			}
			WorldCard cardToPlay = self.deckCollection().deck1().peek();
			assert cardToPlay.card() == MEANDER;
			self.addNextPlay(new CardPlayedEvent(cardToPlay, self, closestTile.get()));
		} else {
			WorldCard cardToPlay = self.deckCollection().deck2().peek();
			assert cardToPlay.card() == MELEE_ATTACK;
			self.addNextPlay(new CardPlayedEvent(cardToPlay, self, nearestCardPlayer));
		}
	}

	@Override
	protected int resetThinkingTime() {
		return (int) (Math.random() * 4) + 10;
	}

}
