package nomadrealms.context.game.world.map.generation.status.points;

import static java.lang.Math.round;
import static nomadrealms.context.game.actor.structure.factory.StructureType.ROCK;
import static nomadrealms.context.game.actor.structure.factory.StructureType.TREE;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.context.game.world.map.generation.status.GenerationStepStatus.POINTS;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Vector2f;
import nomadrealms.context.game.actor.structure.factory.StructureType;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.status.GenerationStep;
import nomadrealms.context.game.world.map.generation.status.GenerationStepStatus;
import nomadrealms.context.game.world.map.generation.status.biome.nomenclature.BiomeVariantType;
import nomadrealms.context.game.world.map.generation.status.points.point.PointOfInterest;

/**
 * Generates the points of interest for the zone.
 */
public class PointsGenerationStep extends GenerationStep {

	private List<PointOfInterest> points;

	/**
	 * No-arg constructor for serialization.
	 */
	public PointsGenerationStep() {
		super(null, 0);
	}

	public PointsGenerationStep(Zone zone, long worldSeed) {
		super(zone, worldSeed);
	}

	@Override
	public GenerationStepStatus status() {
		return POINTS;
	}

	@Override
	public void generate(Zone[][] surrounding) {
		int numPoints = round(zone.nextRandomFloat() * 50);
		points = new ArrayList<>();
		for (int i = 0; i < numPoints; i++) {
			float x = zone.nextRandomFloat();
			float y = zone.nextRandomFloat();
			int tileX = (int) (x * ZONE_SIZE * CHUNK_SIZE);
			int tileY = (int) (y * ZONE_SIZE * CHUNK_SIZE);
			StructureType type = getStructureType(zone.biomeGenerationStep().biomes()[tileX][tileY]);
			if (type != null) {
				points.add(new PointOfInterest(new Vector2f(x, y), 0, 1, type));
			}
		}
	}

	private StructureType getStructureType(BiomeVariantType biome) {
		switch (biome) {
			case FOREST:
			case TROPICAL_RAINFOREST:
			case TEMPERATE_RAINFOREST:
			case DRY_RAINFOREST:
			case JUNGLE:
			case TAIGA:
				return TREE;
			case MOUNTAINS:
			case SNOWY_MOUNTAINS:
			case HILLS:
				return ROCK;
			default:
				return null;
		}
	}

	public List<PointOfInterest> points() {
		return points;
	}

}
