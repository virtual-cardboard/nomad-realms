package nomadrealms.context.game.card.expression;

import java.util.Collections;
import java.util.List;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.card.intent.MeleeDamageIntent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class MeleeDamageExpression implements CardExpression {

	private final int amount;

	public MeleeDamageExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		return Collections.singletonList(new MeleeDamageIntent(target, source, amount));
	}

}
