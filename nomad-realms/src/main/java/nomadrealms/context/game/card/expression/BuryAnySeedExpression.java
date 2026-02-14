package nomadrealms.context.game.card.expression;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static nomadrealms.context.game.item.ItemTag.SEED;

import java.util.List;
import java.util.Optional;

import nomadrealms.context.game.actor.types.HasInventory;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.BuryItemEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;

public class BuryAnySeedExpression implements CardExpression {

	public BuryAnySeedExpression() {
	}

	public static BuryAnySeedExpression buryAnySeed() {
		return new BuryAnySeedExpression();
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		Inventory inventory = ((HasInventory) source).inventory();
		Optional<WorldItem> seed = inventory.items().stream()
				.filter(item -> item.item().tags().contains(SEED))
				.findAny();
		if (seed.isPresent()) {
			return singletonList(new BuryItemEffect(source, seed.get(), source.tile()));
		} else {
			return emptyList();
		}
	}

}
