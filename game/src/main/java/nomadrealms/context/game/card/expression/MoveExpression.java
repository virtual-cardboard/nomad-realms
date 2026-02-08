package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.MoveEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class MoveExpression implements CardExpression {

	int delay = 0;

	public MoveExpression(int delay) {
		this.delay = delay;
	}

	public static MoveExpression move(int delay) {
		return new MoveExpression(delay);
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new MoveEffect(source, (Tile) target, delay));
	}

}
