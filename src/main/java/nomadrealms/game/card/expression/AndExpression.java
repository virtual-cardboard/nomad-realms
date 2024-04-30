package nomadrealms.game.card.expression;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.util.List;

import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.actor.CardPlayer;
import nomadrealms.game.world.World;

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
