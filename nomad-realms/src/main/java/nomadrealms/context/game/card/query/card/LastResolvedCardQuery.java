package nomadrealms.context.game.card.query.card;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

public class LastResolvedCardQuery implements Query<WorldCard> {

	private final Query<CardPlayer> player;

	public LastResolvedCardQuery(Query<CardPlayer> player) {
		this.player = player;
	}

	@Override
	public List<WorldCard> find(EffectContext context) {
		return player.find(context).stream()
				.map(CardPlayer::lastResolvedCard)
				.filter(Objects::nonNull)
				.collect(toList());
	}

}
