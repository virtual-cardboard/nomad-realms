package nomadrealms.context.game.card.condition;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class RangeCondition implements Condition {

	private final int distance;

	public RangeCondition(int distance) {
		this.distance = distance;
	}

	@Override
	public boolean test(World world, Target target, Actor source) {
		if (target == null || target.tile() == null || source == null || source.tile() == null) {
			return false;
		}
		return source.tile().coord().distanceTo(target.tile().coord()) <= distance;
	}

	public int distance() {
		return distance;
	}

}
