package nomadrealms.game.actor;

import nomadrealms.game.event.Target;
import nomadrealms.game.world.map.tile.Tile;

public interface HasPosition extends Target {

	public void move(Tile target);

	public Tile tile();

}
