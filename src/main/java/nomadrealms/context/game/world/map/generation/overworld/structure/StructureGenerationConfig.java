package nomadrealms.context.game.world.map.generation.overworld.structure;

import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;

public abstract class StructureGenerationConfig {

	public abstract Structure placeStructure(TileCoordinate coord, BiomeParameters biome);

}
