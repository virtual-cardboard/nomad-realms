package nomadrealms.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.MoveIntent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

public class MoveExpression implements CardExpression {

	int delay = 0;

	public MoveExpression(int delay) {
		this.delay = delay;
	}

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		return singletonList(new MoveIntent(source, (Tile) target, delay));
	}

}
