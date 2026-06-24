package nomadrealms.context.game.world.architecture;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;

public abstract class Architecture {

	public abstract void place(World world, TileCoordinate coord);

}
