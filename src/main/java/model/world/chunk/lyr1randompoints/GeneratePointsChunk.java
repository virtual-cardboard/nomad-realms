package model.world.chunk.lyr1randompoints;

import java.util.Random;

import common.math.Vector2i;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.lyr0generatebiomes.GenerateBiomesChunk;
import model.world.chunk.lyr2relocatepoints.RelocatePointsChunk;
import model.world.chunk.lyr3generateActors.PointOfInterest;

public class GeneratePointsChunk extends GenerateBiomesChunk {

	public static final int NUM_NODES = 5;
	public static final double MIN_RADIUS = 1;
	public static final double MAX_RADIUS = 8;

	protected PointOfInterest[] points;

	public GeneratePointsChunk(Vector2i pos) {
		super(pos);
	}

	public static GeneratePointsChunk create(Vector2i pos, GenerateBiomesChunk prev, long worldSeed) {
		GeneratePointsChunk c = new GeneratePointsChunk(pos);
		prev.cloneDataTo(c);
		c.points = new PointOfInterest[NUM_NODES];
		long randSeed = ((long) pos.x << 32 | (pos.y & 0xffffffffL)) ^ worldSeed;
		Random rand = new Random(randSeed);
		for (int i = 0; i < NUM_NODES; i++) {
			double x = rand.nextDouble() * CHUNK_SIDE_LENGTH;
			double y = rand.nextDouble() * CHUNK_SIDE_LENGTH;
			double n = rand.nextDouble();
			double r = Math.pow(2, 10 * (n - 1)) * (MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
			c.points[i] = new PointOfInterest(x, y, r);
		}
		return c;
	}

	@Override
	public RelocatePointsChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed) {
		return RelocatePointsChunk.create(pos(), this, neighbours, worldSeed);
	}

	public <T extends GeneratePointsChunk> void cloneDataTo(T c) {
		c.points = points;
		super.cloneDataTo(c);
	}

	@Override
	public int layer() {
		return 1;
	}

	public PointOfInterest[] points() {
		return points;
	}

	protected void setPoints(PointOfInterest[] nodes) {
		this.points = nodes;
	}

}
