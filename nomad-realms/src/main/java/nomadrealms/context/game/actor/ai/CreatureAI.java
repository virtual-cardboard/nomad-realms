package nomadrealms.context.game.actor.ai;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.condition.Condition;
import nomadrealms.context.game.card.condition.RangeCondition;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.map.area.Tile;

/**
 * AI for creatures that are defined declaratively. This class is intentionally not abstract so that creatures can
 * be defined by their name, image, health, and cards without needing a specific AI class.
 *
 * @author Lunkle
 */
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
		WorldCard cardToPlay = self.deckCollection().deck1().draw();
		if (cardToPlay == null) {
			return;
		}

		List<Target> validTargets = getValidTargets(state, cardToPlay);
		if (validTargets.isEmpty()) {
			// If no valid targets, we need to handle the card.
			// In nomad realms, cards usually fizzle or aren't played if no targets.
			// Since we already drew it, if we don't play it, it's effectively discarded.
			return;
		}

		Target target = validTargets.get((int) (Math.random() * validTargets.size()));
		self.addNextPlay(new CardPlayedEvent(cardToPlay, self, target));
	}

	private List<Target> getValidTargets(GameState state, WorldCard card) {
		switch (card.card().targetingInfo().targetType()) {
			case HEXAGON:
				return getHexagonTargets(state, card);
			case CARD_PLAYER:
				return getCardPlayerTargets(state, card);
			case NONE:
				return singletonList(self);
			default:
				return emptyList();
		}
	}

	private int getRange(WorldCard card) {
		for (Condition condition : card.card().targetingInfo().conditions()) {
			if (condition instanceof RangeCondition) {
				return ((RangeCondition) condition).distance();
			}
		}
		return 0;
	}

	private List<Target> getHexagonTargets(GameState state, WorldCard card) {
		List<Target> targets = new ArrayList<>();
		int range = getRange(card);
		// Simple approach: BFS to find all tiles within range
		List<Tile> frontier = new ArrayList<>();
		frontier.add(self.tile());
		List<Tile> visited = new ArrayList<>();
		visited.add(self.tile());

		for (int i = 0; i < range; i++) {
			List<Tile> nextFrontier = new ArrayList<>();
			for (Tile tile : frontier) {
				addIfValid(nextFrontier, visited, tile.ul(state.world));
				addIfValid(nextFrontier, visited, tile.um(state.world));
				addIfValid(nextFrontier, visited, tile.ur(state.world));
				addIfValid(nextFrontier, visited, tile.dl(state.world));
				addIfValid(nextFrontier, visited, tile.dm(state.world));
				addIfValid(nextFrontier, visited, tile.dr(state.world));
			}
			frontier = nextFrontier;
		}
		targets.addAll(visited);
		targets.remove(self.tile());

		// Filter by conditions
		targets.removeIf(target -> {
			for (Condition condition : card.card().targetingInfo().conditions()) {
				if (!condition.test(state.world, target, self)) {
					return true;
				}
			}
			return false;
		});

		return targets;
	}

	private void addIfValid(List<Tile> nextFrontier, List<Tile> visited, Tile tile) {
		if (tile != null && !visited.contains(tile)) {
			nextFrontier.add(tile);
			visited.add(tile);
		}
	}

	private List<Target> getCardPlayerTargets(GameState state, WorldCard card) {
		List<Target> targets = new ArrayList<>();
		int range = getRange(card);
		for (Actor actor : state.world.actors) {
			if (actor instanceof CardPlayer && !actor.isDestroyed()) {
				if (actor.tile().coord().distanceTo(self.tile().coord()) <= range) {
					boolean allConditionsMet = true;
					for (Condition condition : card.card().targetingInfo().conditions()) {
						if (!condition.test(state.world, actor, self)) {
							allConditionsMet = false;
							break;
						}
					}
					if (allConditionsMet) {
						targets.add(actor);
					}
				}
			}
		}
		return targets;
	}

	@Override
	protected int resetThinkingTime() {
		return (int) (Math.random() * 10) + 20;
	}

}
