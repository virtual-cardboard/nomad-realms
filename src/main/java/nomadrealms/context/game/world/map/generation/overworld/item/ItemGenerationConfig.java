package nomadrealms.context.game.world.map.generation.overworld.item;

import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;

public abstract class ItemGenerationConfig {

	public abstract Item placeItem(TileCoordinate coord, BiomeParameters biome);

}
