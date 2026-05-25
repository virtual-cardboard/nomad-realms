package nomadrealms.context.game.card.effect;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static nomadrealms.context.game.card.target.TargetType.CARD_PLAYER;
import static nomadrealms.context.game.card.target.TargetType.HEXAGON;
import static nomadrealms.context.game.card.target.TargetType.NONE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.condition.RangeCondition;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.World;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.event.Target;

public class PlayCardEffect extends Effect {

	private final WorldCard cardToPlay;

	public PlayCardEffect(Actor source, WorldCard cardToPlay) {
		super(source);
		this.cardToPlay = cardToPlay;
	}

	@Override
	public void resolve(World world) {
		CardPlayer cardPlayer = (CardPlayer) source();
		Target target = getRandomValidTarget(world, cardPlayer, cardToPlay);
		cardToPlay.deck().removeCard(cardToPlay);
		// TODO: add an animation
		cardPlayer.cardStack().add(new CardPlayedEvent(cardToPlay, cardPlayer, target));
	}

	private Target getRandomValidTarget(World world, CardPlayer source, WorldCard card) {
		TargetingInfo targetingInfo = card.card().targetingInfo();
		if (targetingInfo.targetType() == NONE) {
			return null;
		}
		List<Target> validTargets = getValidTargets(world, source, card);
		if (validTargets.isEmpty()) {
			return null;
		}
		return validTargets.get(source.tile().chunk().zone().rng().nextInt(validTargets.size()));
	}

	public static List<Target> getValidTargets(World world, CardPlayer source, WorldCard card) {
		TargetingInfo targetingInfo = card.card().targetingInfo();
		int range = targetingInfo.conditions().stream()
				.filter(c -> c instanceof RangeCondition)
				.map(c -> ((RangeCondition) c).distance())
				.findFirst()
				.orElse(Integer.MAX_VALUE); // Default range if not specified

		List<Tile> tiles;
		if (range <= 10) {
			tiles = new TilesInRadiusQuery(range).find(new EffectContext().world(world).source(source).target(source).card(card));
		} else {
			tiles = new ArrayList<>();
			List<Chunk> chunks = source.tile().chunk().getSurroundingChunks();
			for (Chunk chunk : chunks) {
				tiles.addAll(chunk.tiles());
			}
		}

		List<Target> potentialTargets = new ArrayList<>();
		if (targetingInfo.targetType() == CARD_PLAYER) {
			tiles.stream().map(Tile::actor).filter(Objects::nonNull).forEach(potentialTargets::add);
		} else if (targetingInfo.targetType() == HEXAGON) {
			potentialTargets.addAll(tiles);
		}
		return potentialTargets.stream()
				.filter(t -> targetingInfo.conditions().stream().allMatch(c -> c.test(world, t, source)))
				.collect(toList());
	}

}
