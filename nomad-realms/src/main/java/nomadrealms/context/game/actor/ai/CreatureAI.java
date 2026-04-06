package nomadrealms.context.game.actor.ai;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.condition.Condition;
import nomadrealms.context.game.card.condition.RangeCondition;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.card.target.TargetType;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

import java.util.List;
import java.util.stream.Collectors;

public class CreatureAI extends CardPlayerAI {

	/**
	 * No-arg constructor for serialization.
	 */
	protected CreatureAI() {
	}

	public CreatureAI(CardPlayer self) {
		super(self);
	}

	@Override
	public void update(GameState state) {
		if (!self.cardStack().getCards().isEmpty()) {
			return;
		}
		WorldCard cardToPlay = self.deckCollection().deck1().peek();
		if (cardToPlay == null) {
			return;
		}

		if (self.mana() < cardToPlay.card().manaCost()) {
			return;
		}

		Target target = null;
		if (cardToPlay.card().targetingInfo().targetType() != TargetType.NONE) {
			int range = cardToPlay.card().targetingInfo().conditions().stream()
					.filter(c -> c instanceof RangeCondition)
					.map(c -> (RangeCondition) c)
					.mapToInt(RangeCondition::distance)
					.findFirst()
					.orElse(-1);
			if (range == -1) {
				throw new RuntimeException("Card requires target but has no RangeCondition: " + cardToPlay.card().title());
			}
			List<Tile> validTiles = new TilesInRadiusQuery(range).find(new EffectContext().world(state.world).source(self));
			List<Target> validTargets = getValidTargets(state, cardToPlay, validTiles);

			if (validTargets.isEmpty()) {
				return;
			}
			target = validTargets.get((int) (Math.random() * validTargets.size()));
		}

		self.addNextPlay(new CardPlayedEvent(cardToPlay, self, target));
	}

	private List<Target> getValidTargets(GameState state, WorldCard card, List<Tile> tiles) {
		if (card.card().targetingInfo().targetType() == TargetType.HEXAGON) {
			return tiles.stream()
					.filter(tile -> {
						for (Condition condition : card.card().targetingInfo().conditions()) {
							if (!condition.test(state.world, tile, self)) {
								return false;
							}
						}
						return true;
					})
					.collect(Collectors.toList());
		} else if (card.card().targetingInfo().targetType() == TargetType.CARD_PLAYER) {
			return tiles.stream()
					.filter(tile -> tile.actor() instanceof CardPlayer)
					.map(tile -> (CardPlayer) tile.actor())
					.filter(actor -> {
						for (Condition condition : card.card().targetingInfo().conditions()) {
							if (!condition.test(state.world, actor, self)) {
								return false;
							}
						}
						return true;
					})
					.collect(Collectors.toList());
		}
		return List.of();
	}

	@Override
	protected int resetThinkingTime() {
		return 10;
	}

}
