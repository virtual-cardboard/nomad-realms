package nomadrealms.context.game.card.query.actor;

import static java.util.stream.Collectors.toList;

import java.util.List;

import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class StatusCountQuery implements Query<Integer> {

	private final StatusEffect statusEffect;
	private final Query<CardPlayer> player;

	public StatusCountQuery(StatusEffect statusEffect, Query<CardPlayer> player) {
		this.statusEffect = statusEffect;
		this.player = player;
	}

	@Override
	public List<Integer> find(World world, CardPlayer source, Target target) {
		return player.find(world, source, target).stream()
				.map(cardPlayer -> cardPlayer.status().count(statusEffect))
				.collect(toList());
	}
}
