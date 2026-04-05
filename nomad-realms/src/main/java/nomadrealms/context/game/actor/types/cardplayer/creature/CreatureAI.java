package nomadrealms.context.game.actor.types.cardplayer.creature;

import static java.util.stream.Collectors.toList;
import static nomadrealms.context.game.card.target.TargetType.NONE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.ai.CardPlayerAI;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.condition.Condition;
import nomadrealms.context.game.card.condition.RangeCondition;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.map.area.Tile;

public class CreatureAI extends CardPlayerAI {

	private final Random random = new Random();

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
		if (cardToPlay.card().targetingInfo().targetType() != NONE) {
			int range = cardToPlay.card().targetingInfo().conditions().stream()
					.filter(c -> c instanceof RangeCondition)
					.map(c -> (RangeCondition) c)
					.mapToInt(RangeCondition::distance)
					.findFirst()
					.orElseThrow(() -> new RuntimeException("Card requires targeting but has no range condition: " + cardToPlay.card().title()));

			List<Tile> tilesInRange = new TilesInRadiusQuery(range).find(state.world, self, null, cardToPlay);
			List<Target> validTargets = new ArrayList<>();

			for (Tile tile : tilesInRange) {
				if (isValid(state, cardToPlay, tile)) {
					validTargets.add(tile);
				}
				if (tile.actor() != null && isValid(state, cardToPlay, tile.actor())) {
					validTargets.add(tile.actor());
				}
			}

			if (validTargets.isEmpty()) {
				return;
			}
			target = validTargets.get(random.nextInt(validTargets.size()));
		}

		self.addNextPlay(new CardPlayedEvent(cardToPlay, self, target));
	}

	private boolean isValid(GameState state, WorldCard card, Target target) {
		for (Condition condition : card.card().targetingInfo().conditions()) {
			if (!condition.test(state.world, target, self)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected int resetThinkingTime() {
		return 10;
	}

}
