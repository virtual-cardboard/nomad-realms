package model.world.chunk.lyr1generatenodes;

import java.util.Random;

import common.math.Vector2i;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.lyr0generatebiomes.GenerateBiomesChunk;
import model.world.chunk.lyr2relocatenodes.RelocateNodesChunk;
import model.world.chunk.lyr3generateActors.ActorClusterNode;

public class GenerateNodesChunk extends GenerateBiomesChunk {

	public static final int NUM_NODES = 5;
	public static final double MIN_RADIUS = 1;
	public static final double MAX_RADIUS = 8;

	protected ActorClusterNode[] nodes;

	public GenerateNodesChunk(Vector2i pos) {
		super(pos);
	}

	public static GenerateNodesChunk create(Vector2i pos, GenerateBiomesChunk prev, long worldSeed) {
		GenerateNodesChunk c = new GenerateNodesChunk(pos);
		prev.cloneDataTo(c);
		c.nodes = new ActorClusterNode[NUM_NODES];
		long randSeed = ((long) pos.x << 32 | (pos.y & 0xffffffffL)) ^ worldSeed;
		Random rand = new Random(randSeed);
		for (int i = 0; i < NUM_NODES; i++) {
			double x = rand.nextDouble() * CHUNK_SIDE_LENGTH;
			double y = rand.nextDouble() * CHUNK_SIDE_LENGTH;
			double n = rand.nextDouble();
			double r = Math.pow(2, 10 * (n - 1)) * (MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
			c.nodes[i] = new ActorClusterNode(x, y, r);
		}
		return c;
	}

	@Override
	public RelocateNodesChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed) {
		return RelocateNodesChunk.create(pos(), this, neighbours, worldSeed);
	}

	public <T extends GenerateNodesChunk> void cloneDataTo(T c) {
		c.nodes = nodes;
		super.cloneDataTo(c);
	}

	@Override
	public int layer() {
		return 1;
	}

	public ActorClusterNode[] nodes() {
		return nodes;
	}

	protected void setNodes(ActorClusterNode[] nodes) {
		this.nodes = nodes;
	}

}
