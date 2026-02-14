package nomadrealms.context.game.card.condition;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class EmptyCondition implements Condition {

	private final Query<?> query;

	public EmptyCondition(Query<?> query) {
		this.query = query;
	}

	@Override
	public boolean test(World world, Target target, CardPlayer source) {
		return query.find(world, source, target).isEmpty();
	}

	@Override
	public boolean test(World world, Target target, CardPlayer source, Structure structure) {
		return query.find(world, structure, target).isEmpty();
	}

}
