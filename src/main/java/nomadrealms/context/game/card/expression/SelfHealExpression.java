package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.intent.HealIntent;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;

public class SelfHealExpression implements CardExpression {

	private final int amount;

	public SelfHealExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		return singletonList(new HealIntent(source, source, amount));
	}

}
