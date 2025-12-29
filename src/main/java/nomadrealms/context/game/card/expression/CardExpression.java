package nomadrealms.context.game.card.expression;

import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;

import java.util.List;

public interface CardExpression {

	public List<Effect> effects(World world, Target target, CardPlayer source);

}
