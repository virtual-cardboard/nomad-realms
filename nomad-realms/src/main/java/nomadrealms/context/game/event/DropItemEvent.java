package nomadrealms.context.game.event;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.ui.custom.game.GameInterface;

@Derializable
public class DropItemEvent implements InputEvent {

	private WorldItem item;
	private Actor source;
	private Tile tile;

	protected DropItemEvent() {
	}

	public DropItemEvent(WorldItem item, Actor source, Tile tile) {
		this.item = item;
		this.source = source;
		this.tile = tile;
	}

	public WorldItem item() {
		return item;
	}

	public Actor source() {
		return source;
	}

	public Tile tile() {
		return tile;
	}

	@Override
	public void resolve(World world) {
		world.resolve(this);
	}

	@Override
	public void resolve(GameInterface ui) {
		ui.resolve(this);
	}

	@Override
	public String toString() {
		return "DropItemEvent{" +
				"item=" + item.item() +
				", source=" + source +
				'}';
	}
}
