package nomadrealms.context.game.actor;

import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.map.area.Tile;

public interface HasPosition extends Target {

	public default void move(Tile target) {
		if (target.hasActor()) {
			throw new IllegalStateException("Cannot move to occupied tile: " + target.coord());
		}
		if (tile() != null) {
			tile().removeActor();
		}
		target.actor((Actor) this);
		previousTile(tile());
		tile(target);
	}

	public Tile tile();

	public void tile(Tile tile);

	public Tile previousTile();

	public void previousTile(Tile tile);
}
