package nomadrealms.context.game.actor.types;

import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.map.area.Tile;

public interface HasPosition extends Target {

	public default boolean move(Tile target) {
		previousTile(tile());
		tile(target);
		return true;
	}

	public Tile tile();

	public void tile(Tile tile);

	public Tile previousTile();

	public void previousTile(Tile tile);
}
