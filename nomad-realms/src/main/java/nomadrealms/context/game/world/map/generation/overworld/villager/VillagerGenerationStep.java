package nomadrealms.context.game.world.map.generation.overworld.villager;

import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.point.POIType;
import nomadrealms.context.game.world.map.generation.overworld.points.point.PointOfInterest;

public class VillagerGenerationStep extends GenerationStep {

	private final CardPlayer[][] villagers = new CardPlayer[ZONE_SIZE * CHUNK_SIZE][ZONE_SIZE * CHUNK_SIZE];

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
				villagers[x][y] = new VillageChief("Villager", null);
			}
		}
	}

	public CardPlayer[][] villagers() {
		return villagers;
	}

}
