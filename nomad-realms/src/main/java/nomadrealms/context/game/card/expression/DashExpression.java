package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.DashEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

public class DashExpression implements CardExpression {

	int duration = 0;

	public DashExpression(int duration) {
		this.duration = duration;
	}

	public static DashExpression dash(int duration) {
		return new DashExpression(duration);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new DashEffect((CardPlayer) context.source(), (Tile) context.target(), duration));
	}

}
