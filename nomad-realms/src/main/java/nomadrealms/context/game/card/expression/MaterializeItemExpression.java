package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.MaterializeItemEffect;
import nomadrealms.context.game.item.Item;
import nomadrealms.event.game.effect.EffectContext;

public class MaterializeItemExpression implements CardExpression {

	private final Item item;

	public MaterializeItemExpression(Item item) {
		this.item = item;
	}

	public static MaterializeItemExpression materializeItem(Item item) {
		return new MaterializeItemExpression(item);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new MaterializeItemEffect((CardPlayer) context.source(), item));
	}

}
