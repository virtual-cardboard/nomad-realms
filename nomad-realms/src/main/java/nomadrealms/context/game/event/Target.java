package nomadrealms.context.game.event;

import engine.serialization.Derializable;
import nomadrealms.context.game.world.map.area.Tile;

@Derializable
public interface Target {

	Tile tile();

}
