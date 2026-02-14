package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.GatherEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class GatherExpression implements CardExpression {

	private final int range;

	public GatherExpression(int range) {
		this.range = range;
	}

	public static GatherExpression gather(int range) {
		return new GatherExpression(range);
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		return singletonList(new GatherEffect(source, source.tile(), range));
	}

}