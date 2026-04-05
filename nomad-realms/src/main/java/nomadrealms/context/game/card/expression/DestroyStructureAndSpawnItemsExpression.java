package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.DestroyStructureAndSpawnItemsEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.world.World;

public class DestroyStructureAndSpawnItemsExpression implements CardExpression {

	private final Item item;
	private final int count;

	public DestroyStructureAndSpawnItemsExpression(Item item, int count) {
		this.item = item;
		this.count = count;
	}

	public static DestroyStructureAndSpawnItemsExpression destroyStructureAndSpawnItems(Item item, int count) {
		return new DestroyStructureAndSpawnItemsExpression(item, count);
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new DestroyStructureAndSpawnItemsEffect(source, (Actor) target, item, count));
	}

}
