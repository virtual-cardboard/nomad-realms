package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.TeleportEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class TeleportExpression implements CardExpression {

	int delay = 0;

	public TeleportExpression(int delay) {
		this.delay = delay;
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new TeleportEffect(source, source, (Tile) target, delay));
	}

}
