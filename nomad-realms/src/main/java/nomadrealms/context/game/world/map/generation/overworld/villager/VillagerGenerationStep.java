package nomadrealms.context.game.world.map.generation.overworld.villager;

import static java.util.Arrays.asList;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.point.POIType;
import nomadrealms.context.game.world.map.generation.overworld.points.point.PointOfInterest;
import nomadrealms.context.game.world.map.tile.SoilTile;

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
	public void generate(Zone[][] surrounding, MapGenerationStrategy strategy) {
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

				Tile tile = zone.getTile(tileCoord);
				if (tile.actor() != null) {
					tile.clearActor();
				}
				tile.actor(new VillageChief("Villager"));

				List<TileCoordinate> neighbors = asList(
						tileCoord.ul(), tileCoord.um(), tileCoord.ur(),
						tileCoord.dl(), tileCoord.dm(), tileCoord.dr()
				);
				for (TileCoordinate neighborCoord : neighbors) {
					if (!neighborCoord.zone().equals(zone.coord())) {
						continue;
					}
					Tile neighborTile = zone.getTile(neighborCoord);
					SoilTile soilTile = new SoilTile(neighborTile.chunk(), neighborCoord);
					neighborTile.copyStateTo(soilTile);
					neighborTile.clearActor();
					neighborTile.chunk().replace(soilTile);
				}
			}
		}
	}

}
