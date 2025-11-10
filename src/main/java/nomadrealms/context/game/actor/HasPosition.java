package nomadrealms.context.game.actor;

import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.map.area.Tile;

public interface HasPosition extends Target {

	public default void move(Tile target) {
		tile(target);
	}

	public Tile tile();

	public void tile(Tile tile);

}
