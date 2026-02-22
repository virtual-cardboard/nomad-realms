package nomadrealms.context.game.world.map.generation.overworld.structure;

import static java.util.Arrays.asList;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;
import nomadrealms.context.game.world.map.generation.overworld.structure.config.RockGenerationConfig;
import nomadrealms.context.game.world.map.generation.overworld.structure.config.TreeGenerationConfig;

public class StructureGenerationStep extends GenerationStep {

	/**
	 * List of structure generation parameters for each structure type.
	 */
	private List<StructureGenerationConfig> structureParameters;

	/**
	 * No-arg constructor for serialization.
	 */
	protected StructureGenerationStep() {
		super(null, 0);
	}

	public StructureGenerationStep(Zone zone, MapGenerationStrategy strategy) {
		super(zone, strategy.parameters().seed());
		structureParameters = new ArrayList<>(asList(
				new TreeGenerationConfig(strategy.parameters()),
				new RockGenerationConfig(strategy.parameters())
		));
	}

	@Override
	public void generate(Zone[][] surrounding, MapGenerationStrategy strategy) {
		for (ChunkCoordinate[] chunkRow : zone.coord().chunkCoordinates()) {
			for (ChunkCoordinate chunkCoord : chunkRow) {
				for (TileCoordinate[] tileRow : chunkCoord.tileCoordinates()) {
					for (TileCoordinate tileCoord : tileRow) {
						for (StructureGenerationConfig params : structureParameters) {
							BiomeParameters biomeParameters = zone.biomeGenerationStep().parametersAt(tileCoord);
							Structure structure = params.placeStructure(tileCoord, biomeParameters);
							if (structure != null) {
								zone.getTile(tileCoord).actor(structure);
								break;
							}
						}
					}
				}
			}
		}
	}
}
