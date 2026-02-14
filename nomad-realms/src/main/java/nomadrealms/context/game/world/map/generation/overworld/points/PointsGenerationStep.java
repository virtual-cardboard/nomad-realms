package nomadrealms.context.game.world.map.generation.overworld.points;

import static java.lang.Math.round;

import static nomadrealms.context.game.world.map.generation.overworld.points.point.POIType.DEFAULT;
import static nomadrealms.context.game.world.map.generation.overworld.points.point.POIType.VILLAGE;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Vector2f;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.point.PointOfInterest;

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

	public PointsGenerationStep(Zone zone, MapGenerationStrategy strategy) {
		super(zone, strategy.parameters().seed());
	}

	@Override
	public void generate(Zone[][] surrounding, MapGenerationStrategy strategy) {
		int numPoints = round(zone.nextRandomFloat() * 5);
		points = new ArrayList<>();
		for (int i = 0; i < numPoints; i++) {
			float x = zone.nextRandomFloat();
			float y = zone.nextRandomFloat();
			points.add(new PointOfInterest(new Vector2f(x, y), 0, 1, i == 0 && zone.nextRandomFloat() < 0.2f ? VILLAGE : DEFAULT));
		}
	}

	public List<PointOfInterest> points() {
		return points;
	}

}
