package com.nomadrealms.algorithm.generation.map;

import com.nomadrealms.logic.game.world.World;
import com.nomadrealms.logic.game.world.area.Zone;
import com.nomadrealms.math.coordinate.map.ZoneCoordinate;

/**
 * Generates a zone using a noise algorithm.
 */
public class LayeredGenerationStrategy implements MapGenerationStrategy {

	private long seed;

	public LayeredGenerationStrategy(long seed) {
		this.seed = seed;
	}

	@Override
	public Zone generateZone(World world, ZoneCoordinate coord) {
		OpenSimplexNoise noise = new OpenSimplexNoise(seed);

		for (int i = -5; i < 5; i++) {
			for (int j = -5; j < 5; j++) {
//				Region region = world.getZone(x, y);
			}
		}

		// layer-based map generation
		generateLandMarks(world, coord);
		perturbLandmarks(world, coord);
		calculateSizes(world, coord);
		calculateConnections(world, coord);
		refineLandmarks(world, coord);

		// final pass
		refineZone(world, coord);
		return new Zone(world, coord);
	}

	private void generateLandMarks(World world, ZoneCoordinate coord) {
	}

	private void perturbLandmarks(World world, ZoneCoordinate coord) {

	}

	private void calculateSizes(World world, ZoneCoordinate coord) {

	}

	private void calculateConnections(World world, ZoneCoordinate coord) {
	}

	private void refineLandmarks(World world, ZoneCoordinate coord) {
	}

	private void refineZone(World world, ZoneCoordinate coord) {
	}

}
