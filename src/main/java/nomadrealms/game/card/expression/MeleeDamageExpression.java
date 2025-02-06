package nomadrealms.game.card.expression;

import java.util.Collections;
import java.util.List;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.MeleeDamageIntent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.World;

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
