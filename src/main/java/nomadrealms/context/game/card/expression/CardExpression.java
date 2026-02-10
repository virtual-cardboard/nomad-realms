package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public interface CardExpression {

	public default List<CardExpression> list() {
		return singletonList(this);
	}

	public default CardExpression then(CardExpression next) {
		return new ThenExpression(this, next);
	}

	public List<Effect> effects(World world, Target target, CardPlayer source);

}
