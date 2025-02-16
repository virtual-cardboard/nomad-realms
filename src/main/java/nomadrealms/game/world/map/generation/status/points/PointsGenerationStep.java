package nomadrealms.game.world.map.generation.status.points;

import static java.lang.Math.round;
import static nomadrealms.game.world.map.generation.status.GenerationStepStatus.POINTS;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2f;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.generation.status.GenerationStep;
import nomadrealms.game.world.map.generation.status.GenerationStepStatus;
import nomadrealms.game.world.map.generation.status.points.point.PointOfInterest;

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
		int numPoints = round(zone.nextRandomFloat() * 5);
		points = new ArrayList<>();
		for (int i = 0; i < numPoints; i++) {
			float x = zone.nextRandomFloat();
			float y = zone.nextRandomFloat();
			points.add(new PointOfInterest(new Vector2f(x, y), 0, 1));
		}
	}

	public List<PointOfInterest> points() {
		return points;
	}

}
