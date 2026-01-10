package nomadrealms.context.game.card.query.card;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

/**
 * A query expression that can be used by card expressions and intents to find {@link WorldCard WorldCards} in the game
 * world.
 *
 * @author Lunkle
 */
public class LastResolvedCardQuery implements Query<WorldCard> {

	private final Query<CardPlayer> player;

	public LastResolvedCardQuery(Query<CardPlayer> player) {
		this.player = player;
	}

	@Override
	public List<WorldCard> find(World world, CardPlayer source, Target target) {
		return player.find(world, source, target).stream()
				.map(CardPlayer::lastResolvedCard)
				.filter(Objects::nonNull)
				.collect(toList());
	}

}
