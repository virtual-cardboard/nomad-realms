package nomadrealms.context.game.world.map.generation.overworld.villager;

import static java.util.Arrays.asList;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import java.util.List;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.architecture.Village;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.point.POIType;
import nomadrealms.context.game.world.map.generation.overworld.points.point.PointOfInterest;

public class VillagerGenerationStep extends GenerationStep {

	/**
	 * No-arg constructor for serialization.
	 */
	protected VillagerGenerationStep() {
		super(null, 0);
	}

	public VillagerGenerationStep(Zone zone, MapGenerationStrategy strategy) {
		super(zone, strategy.parameters().seed());
	}

	@Override
	public void generate(World world, Zone[][] surrounding, MapGenerationStrategy strategy) {
		for (PointOfInterest poi : zone.pointsGenerationStep().points()) {
			if (poi.type() == POIType.VILLAGE) {
				int x = (int) (poi.position().x() * ZONE_SIZE * CHUNK_SIZE);
				int y = (int) (poi.position().y() * ZONE_SIZE * CHUNK_SIZE);
				x = Math.max(0, Math.min(x, ZONE_SIZE * CHUNK_SIZE - 1));
				y = Math.max(0, Math.min(y, ZONE_SIZE * CHUNK_SIZE - 1));

				int chunkX = x / CHUNK_SIZE;
				int chunkY = y / CHUNK_SIZE;
				int tileX = x % CHUNK_SIZE;
				int tileY = y % CHUNK_SIZE;

				ChunkCoordinate chunkCoord = zone.coord().chunkCoordinates()[chunkX][chunkY];
				TileCoordinate tileCoord = new TileCoordinate(chunkCoord, tileX, tileY);

				Village.INSTANCE.place(world, tileCoord);
			}
		}
	}

}
