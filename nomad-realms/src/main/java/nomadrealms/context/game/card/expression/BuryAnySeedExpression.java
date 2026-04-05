package nomadrealms.context.game.card.expression;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static nomadrealms.context.game.item.ItemTag.SEED;

import java.util.List;
import java.util.Optional;

import nomadrealms.context.game.actor.types.HasInventory;
import nomadrealms.context.game.card.effect.BuryItemEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.event.game.effect.EffectContext;

public class BuryAnySeedExpression implements CardExpression {

	public BuryAnySeedExpression() {
	}

	public static BuryAnySeedExpression buryAnySeed() {
		return new BuryAnySeedExpression();
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		Inventory inventory = ((HasInventory) context.source()).inventory();
		Optional<WorldItem> seed = inventory.items().stream()
				.filter(item -> item.item().tags().contains(SEED))
				.findAny();
		if (seed.isPresent()) {
			return singletonList(new BuryItemEffect((CardPlayer) context.source(), context.source(), seed.get(), context.source().tile()));
		} else {
			return emptyList();
		}
	}

}
