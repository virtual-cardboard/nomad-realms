package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.DashEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class DashExpression implements CardExpression {

	int duration = 0;

	public DashExpression(int duration) {
		this.duration = duration;
	}

	public static DashExpression dash(int duration) {
		return new DashExpression(duration);
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		return singletonList(new DashEffect(source, (Tile) target, duration));
	}

}
