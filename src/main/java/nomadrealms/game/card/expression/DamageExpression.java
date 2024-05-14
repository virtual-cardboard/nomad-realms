package nomadrealms.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.game.card.intent.DamageIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.world.World;

public class DamageExpression implements CardExpression {

	private final int amount;

	public DamageExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		return singletonList(new DamageIntent(target, source, amount));
	}

}
