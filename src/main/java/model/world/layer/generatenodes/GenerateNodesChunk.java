package model.world.layer.generatenodes;

import common.math.Vector2i;
import graphics.noise.OpenSimplexNoise;
import model.world.Seed;
import model.world.TileChunk;
import model.world.layer.actorcluster.ActorClusterNode;
import model.world.layer.relocatenodes.RelocateNodesChunk;

public class GenerateNodesChunk extends TileChunk {

	protected ActorClusterNode[] nodes;

	public GenerateNodesChunk(Vector2i pos) {
		super(pos);
	}

	public static GenerateNodesChunk create(Vector2i pos, long worldSeed) {
		GenerateNodesChunk c = new GenerateNodesChunk(pos);
		c.nodes = new ActorClusterNode[3];
		OpenSimplexNoise nodeSeed = new OpenSimplexNoise(Seed.node(worldSeed));
		for (int i = 0; i < 3; i++) {
			double x = nodeSeed.eval(pos.x, pos.y, 20 * i) * CHUNK_WIDTH;
			double y = nodeSeed.eval(pos.x, pos.y, 20 * i + 1000) * CHUNK_HEIGHT;
			c.nodes[i] = new ActorClusterNode((float) x, (float) y);
		}
		return c;
	}

	@Override
	public RelocateNodesChunk upgrade(TileChunk[][] neighbours, long worldSeed) {
		return RelocateNodesChunk.create(pos(), this, neighbours, worldSeed);
	}

	@Override
	public int layer() {
		return 0;
	}

	public ActorClusterNode[] nodes() {
		return nodes;
	}

	protected void setNodes(ActorClusterNode[] nodes) {
		this.nodes = nodes;
	}

}
