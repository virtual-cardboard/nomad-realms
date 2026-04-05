package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.TeleportEffect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

public class TeleportExpression implements CardExpression {

	int delay = 0;

	public TeleportExpression(int delay) {
		this.delay = delay;
	}

	public static TeleportExpression teleport(int delay) {
		return new TeleportExpression(delay);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new TeleportEffect((CardPlayer) context.source(), (Tile) context.target(), delay));
	}

}
