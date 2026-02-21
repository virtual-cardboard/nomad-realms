package nomadrealms.context.game.card.condition;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public interface Condition {

	public default boolean test(World world, Target target, Actor source) {
		throw new UnsupportedOperationException();
	}

	public default boolean test(World world, Target target, Actor source, Structure structure) {
		return test(world, target, source);
	}

}
