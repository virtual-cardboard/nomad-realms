package nomadrealms.game.world.actor;

import nomadrealms.game.world.map.tile.Tile;

public interface HasPosition {

	public void move(Tile target);

	public Tile tile();

}
