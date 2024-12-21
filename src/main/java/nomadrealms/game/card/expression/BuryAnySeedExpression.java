package nomadrealms.game.card.expression;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static nomadrealms.game.item.ItemTag.SEED;

import java.util.List;
import java.util.Optional;

import nomadrealms.game.actor.HasInventory;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.intent.BuryItemIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.item.Inventory;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;

public class BuryAnySeedExpression implements CardExpression {

	public BuryAnySeedExpression() {
	}

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		Inventory inventory = ((HasInventory) source).inventory();
		Optional<WorldItem> seed = inventory.items().stream()
				.filter(item -> item.item().tags().contains(SEED))
				.findAny();
		if (seed.isPresent()) {
			return singletonList(new BuryItemIntent(source, seed.get()));
		} else {
			return emptyList();
		}
	}

}
