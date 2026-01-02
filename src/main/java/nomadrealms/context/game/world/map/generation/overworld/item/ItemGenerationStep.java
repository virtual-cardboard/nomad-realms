package nomadrealms.context.game.world.map.generation.overworld.item;

import static java.util.Arrays.asList;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;
import nomadrealms.context.game.world.map.generation.overworld.item.config.LogGenerationConfig;

public class ItemGenerationStep extends GenerationStep {

	/**
	 * List of item generation parameters for each item type.
	 */
	private List<ItemGenerationConfig> itemParameters;

	private final Item[][] items = new Item[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];

	/**
	 * No-arg constructor for serialization.
	 */
	protected ItemGenerationStep() {
		super(null, 0);
	}

	public ItemGenerationStep(Zone zone, MapGenerationStrategy strategy) {
		super(zone, strategy.parameters().seed());
		itemParameters = new ArrayList<>(asList(
				new LogGenerationConfig(strategy.parameters())
		));
	}

	@Override
	public void generate(Zone[][] surrounding, MapGenerationStrategy strategy) {
		for (ChunkCoordinate[] chunkRow : zone.coord().chunkCoordinates()) {
			for (ChunkCoordinate chunk : chunkRow) {
				for (TileCoordinate[] tileRow : chunk.tileCoordinates()) {
					for (TileCoordinate tile : tileRow) {
						BiomeParameters biomeParameters = zone.biomeGenerationStep().parametersAt(tile);
						for (ItemGenerationConfig params : itemParameters) {
							Item item = params.placeItem(tile, biomeParameters);
							if (item != null) {
								items[chunk.x() * CHUNK_SIZE + tile.x()][chunk.y() * CHUNK_SIZE + tile.y()] = item;
								break;
							}
						}
					}
				}
			}
		}
	}

	public Item[][] items() {
		return items;
	}
}
