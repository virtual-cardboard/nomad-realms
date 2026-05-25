package nomadrealms.context.game.actor.ai;

import static java.util.Comparator.comparingInt;
import static nomadrealms.context.game.item.Item.OAK_LOG;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.structure.TreeStructure;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

public class VillageLumberjackAI extends CardPlayerAI {

	/**
	 * No-arg constructor for serialization.
	 */
	protected VillageLumberjackAI() {
	}

	public VillageLumberjackAI(CardPlayer self) {
		super(self);
	}

	@Override
	public void update(GameState state) {
		if (!self.cardStack().getCards().isEmpty()) {
			return;
		}

		// 1. Collect OAK_LOG if on current tile
		if (self.tile().items().stream().anyMatch(item -> item.item() == OAK_LOG)) {
			WorldCard gatherCard = self.deckCollection().deck3().peek();
			if (gatherCard != null) {
				self.addNextPlay(new CardPlayedEvent(gatherCard, self, null));
				return;
			}
		}

		// 2. Move to nearby OAK_LOG
		Optional<Tile> logTile = new TilesInRadiusQuery(6).find(new EffectContext().world(state.world).source(self).target(self)).stream()
				.filter(tile -> tile.items().stream().anyMatch(item -> item.item() == OAK_LOG))
				.findFirst();
		if (logTile.isPresent()) {
			WorldCard moveCard = self.deckCollection().deck4().peek();
			if (moveCard != null) {
				self.addNextPlay(new CardPlayedEvent(moveCard, self, logTile.get()));
				return;
			}
		}

		// 3. Cut nearest tree within 10 tiles
		// NOTE: generated trees are attached to tiles (tile.actor(structure)) but may not be
		// registered in state.world.structures. Query tiles in radius and inspect tile.actor().
		Optional<Tile> nearestTreeTile = new TilesInRadiusQuery(10).find(new EffectContext().world(state.world).source(self).target(self)).stream()
			.filter(tile -> tile.actor() instanceof TreeStructure)
			.filter(tile -> !tile.actor().dead())
			.min(comparingInt(t -> t.coord().distanceTo(self.tile().coord())));

		TreeStructure nearestTree = nearestTreeTile.map(t -> (TreeStructure) t.actor()).orElse(null);

		if (nearestTree != null) {
//            System.out.println("Lumberjack found a tree to cut at " + nearestTree.tile().coord());
			WorldCard cutTreeCard = self.deckCollection().deck2().peek();
			if (cutTreeCard != null) {
				self.addNextPlay(new CardPlayedEvent(cutTreeCard, self, nearestTree));
				return;
			}
		}

		// 4. Meander if nothing else to do
		WorldCard meanderCard = self.deckCollection().deck1().peek();
		if (meanderCard != null) {
			List<Tile> neighbors = Stream.of(
							self.tile().dl(state.world),
							self.tile().dm(state.world),
							self.tile().dr(state.world),
							self.tile().ul(state.world),
							self.tile().um(state.world),
							self.tile().ur(state.world)
					)
					.filter(tile -> tile != null && tile.actor() == null)
					.collect(Collectors.toList());
			if (!neighbors.isEmpty()) {
				Tile randomTile = neighbors.get(self.tile().chunk().zone().rng().nextInt(neighbors.size()));
				self.addNextPlay(new CardPlayedEvent(meanderCard, self, randomTile));
			}
		}
	}

	@Override
	protected int resetThinkingTime() {
		return self.tile().chunk().zone().rng().nextInt(5) + 15;
	}

}
