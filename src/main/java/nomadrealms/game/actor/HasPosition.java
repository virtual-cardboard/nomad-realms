package nomadrealms.game.actor;

import nomadrealms.game.event.Target;
import nomadrealms.game.world.map.tile.Tile;

public interface HasPosition extends Target {

	public default void move(Tile target) {
		tile(target);
	}

	public Tile tile();

	public void tile(Tile tile);

}
