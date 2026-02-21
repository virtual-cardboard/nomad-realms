package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.MoveToAdjacentEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class MoveToAdjacentExpression implements CardExpression {

	private final int delay;

	public MoveToAdjacentExpression(int delay) {
		this.delay = delay;
	}

	public static MoveToAdjacentExpression moveToAdjacent(int delay) {
		return new MoveToAdjacentExpression(delay);
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		return singletonList(new MoveToAdjacentEffect(source, target, delay));
	}

}
