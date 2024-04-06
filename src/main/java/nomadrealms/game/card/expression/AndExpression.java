package nomadrealms.game.card.expression;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.util.List;
import java.util.stream.Collectors;

import nomadrealms.game.GameState;
import nomadrealms.game.card.block.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.actor.CardPlayer;

public class AndExpression implements CardExpression {

	private final CardExpression left;
	private final CardExpression right;

	public AndExpression(CardExpression left, CardExpression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public List<Intent> intents(GameState state, Target target, CardPlayer source) {
		return concat(
				left.intents(state, target, source).stream(),
				right.intents(state, target, source).stream()
		).collect(toList());
	}

}
