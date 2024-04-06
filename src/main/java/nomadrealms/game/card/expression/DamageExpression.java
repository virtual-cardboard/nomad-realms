package nomadrealms.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nomadrealms.game.GameState;
import nomadrealms.game.card.intent.DamageIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.actor.CardPlayer;

public class DamageExpression implements CardExpression {

	private final int amount;

	public DamageExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public List<Intent> intents(GameState state, Target target, CardPlayer source) {
		return singletonList(new DamageIntent(target, source, amount));
	}

}
