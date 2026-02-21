package nomadrealms.context.game.card.action;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class DestroyStructureAndSpawnItemsAction extends Action {

	private final Actor target;
	private final Item itemToSpawn;
	private final int count;
	private boolean complete = false;

	public DestroyStructureAndSpawnItemsAction(Actor source, Actor target, Item itemToSpawn, int count) {
		super(source);
		this.target = target;
		this.itemToSpawn = itemToSpawn;
		this.count = count;
	}

	@Override
	public void update(World world) {
		if (target.tile() == null) {
			complete = true;
			return;
		}
		if (source.tile().coord().distanceTo(target.tile().coord()) <= 1) {
			Tile tile = target.tile();
			tile.clearActor();
			target.health(0);
			for (int i = 0; i < count; i++) {
				tile.addItem(new WorldItem(itemToSpawn));
			}
		}
		complete = true;
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	@Override
	public int preDelay() {
		return 0;
	}

	@Override
	public int postDelay() {
		return 0;
	}

}
