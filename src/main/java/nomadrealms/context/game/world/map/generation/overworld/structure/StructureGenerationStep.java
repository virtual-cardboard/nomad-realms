package nomadrealms.context.game.world.map.generation.overworld.structure;

import static java.util.Arrays.asList;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import java.util.List;

import nomadrealms.context.game.actor.structure.Structure;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationParameters;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;
import nomadrealms.context.game.world.map.generation.overworld.structure.config.TreeGenerationConfig;

public class StructureGenerationStep extends GenerationStep {

	/**
	 * List of structure generation parameters for each structure type.
	 */
	private List<StructureGenerationConfig> structureParameters;

	private final Structure[][] structures = new Structure[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];

	/**
	 * No-arg constructor for serialization.
	 */
	protected StructureGenerationStep() {
		super(null, 0);
	}

	public StructureGenerationStep(Zone zone, MapGenerationParameters mapParameters) {
		super(zone, mapParameters.seed());
		structureParameters = asList(
				new TreeGenerationConfig(mapParameters)
		);
	}

	@Override
	public void generate(Zone[][] surrounding, MapGenerationStrategy strategy) {
		System.out.println("Generating structures...");
		for (ChunkCoordinate[] chunkRow : zone.coord().chunkCoordinates()) {
			for (ChunkCoordinate chunk : chunkRow) {
				for (TileCoordinate[] tileRow : chunk.tileCoordinates()) {
					for (TileCoordinate tile : tileRow) {
						for (StructureGenerationConfig params : structureParameters) {
							BiomeParameters biomeParameters = zone.biomeGenerationStep().parametersAt(tile);
							Structure structure = params.placeStructure(tile, biomeParameters);
							if (structure != null) {
								System.out.println("Placed structure at " + tile + " in chunk " + chunk);
							}
							structures[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = structure;
						}
					}
				}
			}
		}
	}

	public Structure[][] structures() {
		return structures;
	}
}
