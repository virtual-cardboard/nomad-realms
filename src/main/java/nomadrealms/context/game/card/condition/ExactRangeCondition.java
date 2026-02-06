package nomadrealms.context.game.card.condition;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class ExactRangeCondition implements Condition {

	private final Query<? extends Target> target1;
	private final Query<? extends Target> target2;
	private final int distance;

	public ExactRangeCondition(Query<? extends Target> target1, Query<? extends Target> target2, int distance) {
		this.target1 = target1;
		this.target2 = target2;
		this.distance = distance;
	}

	@Override
	public boolean test(World world, Target target, CardPlayer source) {
		List<? extends Target> targets1 = target1.find(world, source, target);
		List<? extends Target> targets2 = target2.find(world, source, target);
		if (targets1.isEmpty() || targets2.isEmpty() || targets1.get(0).tile() == null || targets2.get(0).tile() == null) {
			return false;
		}
		return targets1.get(0).tile().coord().distanceTo(targets2.get(0).tile().coord()) == distance;
	}

	public int distance() {
		return distance;
	}

}
