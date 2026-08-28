package nomadrealms.context.game.actor.ai;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static nomadrealms.context.game.card.GameCard.DEBILITATING_FEAR;
import static nomadrealms.context.game.card.GameCard.FEAR;
import static nomadrealms.context.game.card.GameCard.MEANDER;
import static nomadrealms.context.game.card.GameCard.TOTEM_OF_PAIN;
import static nomadrealms.context.game.card.GameCard.VOODOO_HEX;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.WitchBear;
import nomadrealms.context.game.actor.types.structure.TotemOfPainStructure;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

public class WitchBearAI extends CardPlayerAI {

	/**
	 * No-arg constructor for serialization.
	 */
	protected WitchBearAI() {
	}

	public WitchBearAI(CardPlayer self) {
		super(self);
	}

	@Override
	public void update(GameState state) {
		if (!self.cardStack().getCards().isEmpty()) {
			return;
		}

		// Priority 1: Cast TOTEM_OF_PAIN if there's no Totem of Pain structure within radius 5
		boolean hasTotemNearby = new TilesInRadiusQuery(5)
				.find(new EffectContext().world(state.world).source(self).target(self)).stream()
				.anyMatch(tile -> tile.actor() instanceof TotemOfPainStructure);

		if (!hasTotemNearby) {
			WorldCard totemCard = self.deckCollection().deck2().peek();
			if (totemCard != null) {
				assert totemCard.card() == TOTEM_OF_PAIN;
				Optional<Tile> emptyAdjacentTile = Stream.of(
								self.tile().dl(state.world),
								self.tile().dm(state.world),
								self.tile().dr(state.world),
								self.tile().ul(state.world),
								self.tile().um(state.world),
								self.tile().ur(state.world)
						)
						.filter(tile -> tile != null && tile.actor() == null)
						.findAny();
				if (emptyAdjacentTile.isPresent()) {
					self.addNextPlay(new CardPlayedEvent(totemCard, self, emptyAdjacentTile.get()));
					return;
				}
			}
		}

		// Priority 2: Cast DEBILITATING_FEAR on an enemy CardPlayer within range 6 that has FEAR in their card stack
		List<CardPlayer> enemiesWithFear = self.tile().chunk().getSurroundingChunks().stream()
				.flatMap(chunk -> chunk.actors().stream())
				.filter(actor -> !(actor instanceof WitchBear))
				.filter(actor -> actor instanceof CardPlayer)
				.map(actor -> (CardPlayer) actor)
				.filter(actor -> !actor.dead())
				.filter(actor -> actor.tile().coord().distanceTo(self.tile().coord()) <= 6)
				.filter(actor -> actor.cardStack().getCards().stream()
						.anyMatch(entry -> entry.event().card().card() == FEAR))
				.collect(toList());

		if (!enemiesWithFear.isEmpty()) {
			WorldCard debilitatingFearCard = self.deckCollection().deck4().peek();
			if (debilitatingFearCard != null) {
				assert debilitatingFearCard.card() == DEBILITATING_FEAR;
				CardPlayer target = enemiesWithFear.get(0);
				self.addNextPlay(new CardPlayedEvent(debilitatingFearCard, self, target));
				return;
			}
		}

		// Priority 3: Cast VOODOO_HEX if there is an enemy CardPlayer within range 3
		CardPlayer targetInRange3 = self.tile().chunk().getSurroundingChunks().stream()
				.flatMap(chunk -> chunk.actors().stream())
				.filter(actor -> !(actor instanceof WitchBear))
				.filter(actor -> actor instanceof CardPlayer)
				.map(actor -> (CardPlayer) actor)
				.filter(actor -> !actor.dead())
				.filter(actor -> actor.tile().coord().distanceTo(self.tile().coord()) <= 3)
				.findFirst()
				.orElse(null);

		if (targetInRange3 != null) {
			WorldCard voodooHexCard = self.deckCollection().deck3().peek();
			if (voodooHexCard != null) {
				assert voodooHexCard.card() == VOODOO_HEX;
				self.addNextPlay(new CardPlayedEvent(voodooHexCard, self, null));
				return;
			}
		}

		// Priority 4: Move towards nearest non-WitchBear CardPlayer using MEANDER if out of range, or meander randomly
		CardPlayer nearestEnemy = self.tile().chunk().getSurroundingChunks().stream()
				.flatMap(chunk -> chunk.actors().stream())
				.filter(actor -> !(actor instanceof WitchBear))
				.filter(actor -> actor instanceof CardPlayer)
				.map(actor -> (CardPlayer) actor)
				.filter(actor -> !actor.dead())
				.filter(actor -> actor.tile().coord().distanceTo(self.tile().coord()) < 20)
				.min(comparingInt(a -> a.tile().coord().distanceTo(self.tile().coord())))
				.orElse(null);

		WorldCard meanderCard = self.deckCollection().deck1().peek();
		if (meanderCard != null) {
			assert meanderCard.card() == MEANDER;
			Optional<Tile> targetTile;
			if (nearestEnemy != null && nearestEnemy.tile().coord().distanceTo(self.tile().coord()) > 1) {
				targetTile = Stream.of(
								self.tile().dl(state.world),
								self.tile().dm(state.world),
								self.tile().dr(state.world),
								self.tile().ul(state.world),
								self.tile().um(state.world),
								self.tile().ur(state.world)
						)
						.filter(tile -> tile != null && tile.actor() == null)
						.min(comparingInt(t -> t.coord().distanceTo(nearestEnemy.tile().coord())));
			} else {
				targetTile = Stream.of(
								self.tile().dl(state.world),
								self.tile().dm(state.world),
								self.tile().dr(state.world),
								self.tile().ul(state.world),
								self.tile().um(state.world),
								self.tile().ur(state.world)
						)
						.filter(tile -> tile != null && tile.actor() == null)
						.findAny();
			}
			targetTile.ifPresent(tile -> self.addNextPlay(new CardPlayedEvent(meanderCard, self, tile)));
		}
	}

	@Override
	protected int resetThinkingTime() {
		return (int) (Math.random() * 5) + 15;
	}

}
