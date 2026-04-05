package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.WalkEffect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

public class WalkExpression implements CardExpression {

	int delay = 0;

	public WalkExpression(int delay) {
		this.delay = delay;
	}

	public static WalkExpression walk(int delay) {
		return new WalkExpression(delay);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new WalkEffect((CardPlayer) context.source(), (Tile) context.target(), delay));
	}

}
