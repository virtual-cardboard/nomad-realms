package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SpawnFireCircleEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class SpawnParticlesExpression implements CardExpression {

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new SpawnFireCircleEffect(source));
	}

}
