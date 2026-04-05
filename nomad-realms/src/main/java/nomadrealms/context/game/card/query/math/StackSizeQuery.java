package nomadrealms.context.game.card.query.math;

import static java.util.stream.Collectors.toList;

import java.util.List;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

@Derializable
public class StackSizeQuery implements Query<Integer> {

	private Query<CardPlayer> source;

	protected StackSizeQuery() {
	}

	public StackSizeQuery(Query<CardPlayer> source) {
		this.source = source;
	}

	@Override
	public List<Integer> find(EffectContext context) {
		return this.source.find(context).stream()
				.map(player -> player.cardStack().size())
				.collect(toList());
	}
}
