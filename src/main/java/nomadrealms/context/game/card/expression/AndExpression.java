package nomadrealms.context.game.card.expression;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.util.List;

import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;

public class AndExpression implements CardExpression {

	private final CardExpression left;
	private final CardExpression right;

	public AndExpression(CardExpression left, CardExpression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		return concat(
				left.intents(world, target, source).stream(),
				right.intents(world, target, source).stream()
		).collect(toList());
	}

}
