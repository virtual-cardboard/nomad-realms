package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.DestroyStructureAndSpawnItemsAction;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.world.World;

public class DestroyStructureAndSpawnItemsEffect extends Effect {

	private final Actor target;
	private final Item itemToSpawn;
	private final int count;

	public DestroyStructureAndSpawnItemsEffect(CardPlayer source, Actor target, Item itemToSpawn, int count) {
		super(source);
		this.target = target;
		this.itemToSpawn = itemToSpawn;
		this.count = count;
	}

	@Override
	public void resolve(World world) {
		((CardPlayer) source()).queueAction(new DestroyStructureAndSpawnItemsAction((CardPlayer) source(), target, itemToSpawn, count));
	}

}
