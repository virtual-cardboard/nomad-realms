package model.world.chunk.lyr1generatenodes;

import common.math.Vector2i;
import graphics.noise.OpenSimplexNoise;
import model.world.WorldSeed;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.lyr0generatebiomes.GenerateBiomesChunk;
import model.world.chunk.lyr2relocatenodes.RelocateNodesChunk;
import model.world.chunk.lyr3generateActors.ActorClusterNode;

public class GenerateNodesChunk extends GenerateBiomesChunk {

	private static final int NUM_NODES = 5;

	protected ActorClusterNode[] nodes;

	public GenerateNodesChunk(Vector2i pos) {
		super(pos);
	}

	public static GenerateNodesChunk create(Vector2i pos, GenerateBiomesChunk prev, long worldSeed) {
		GenerateNodesChunk c = new GenerateNodesChunk(pos);
		prev.cloneDataTo(c);
		c.nodes = new ActorClusterNode[NUM_NODES];
		OpenSimplexNoise nodeSeed = new OpenSimplexNoise(WorldSeed.node(worldSeed));
		for (int i = 0; i < NUM_NODES; i++) {
			double x = (nodeSeed.eval(pos.x * 2000, pos.y * 2000, 2000 * i) * 0.5 + 0.5) * CHUNK_SIDE_LENGTH;
			double y = (nodeSeed.eval(pos.x * 2000, pos.y * 2000, 2000 * i + 200000) * 0.5 + 0.5) * CHUNK_SIDE_LENGTH;
			double r = (nodeSeed.eval(pos.x * 2000, pos.y * 2000, 2000 * i + 400000) * 0.5 + 0.5) * 2;
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
