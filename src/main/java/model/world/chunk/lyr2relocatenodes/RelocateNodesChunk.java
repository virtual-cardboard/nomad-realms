package model.world.chunk.lyr2relocatenodes;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2f;
import common.math.Vector2i;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.lyr1generatenodes.GenerateNodesChunk;
import model.world.chunk.lyr3generateActors.ActorClusterNode;
import model.world.chunk.lyr3generateActors.GenerateActorsChunk;

public class RelocateNodesChunk extends GenerateNodesChunk {

	protected ActorClusterNode[] relocatedNodes;

	public RelocateNodesChunk(Vector2i pos) {
		super(pos);
	}

	public static RelocateNodesChunk create(Vector2i pos, GenerateNodesChunk prev, AbstractTileChunk[][] neighbours, long worldSeed) {
		RelocateNodesChunk c = new RelocateNodesChunk(pos);

		prev.cloneDataTo(c);

		List<Vector2f> allNodePositions = new ArrayList<>(9 * NUM_NODES);
		List<Float> radii = new ArrayList<>(9 * NUM_NODES);
		for (AbstractTileChunk[] arr : neighbours) {
			for (AbstractTileChunk chunk : arr) {
				GenerateNodesChunk gnc = (GenerateNodesChunk) chunk;
				for (ActorClusterNode node : gnc.nodes()) {
					Vector2f chunkPosDiff = chunk.pos().sub(pos).toVec2f().scale(CHUNK_SIDE_LENGTH);
					allNodePositions.add(node.pos().add(chunkPosDiff));
					radii.add(node.radius());
				}
			}
		}

		// Movement vectors that will be added to the node positions
		Vector2f[] moveVecs = new Vector2f[c.nodes.length];
		for (int i = 0; i < moveVecs.length; i++) {
			moveVecs[i] = new Vector2f();
		}

		// Edit movement vectors
		for (int i = 0; i < c.nodes.length; i++) {
			ActorClusterNode n = c.nodes[i];
			Vector2f nPos = n.pos();
			for (int j = 0; j < allNodePositions.size(); j++) {
				Vector2f o = allNodePositions.get(j);
				if (!n.pos().equals(o)) {
					Vector2f oToN = nPos.sub(o);
					float dist = oToN.length();
					Vector2f toAdd = oToN.normalise().scale(5 * radii.get(j) / (n.radius() * dist * dist + dist + 4.15f));
					moveVecs[i] = moveVecs[i].add(toAdd);
				}
			}
		}

		// Create list of relocated nodes
		c.relocatedNodes = new ActorClusterNode[c.nodes.length];
		for (int i = 0; i < c.nodes.length; i++) {
			ActorClusterNode n = c.nodes[i];
			c.relocatedNodes[i] = new ActorClusterNode(n.pos().add(moveVecs[i]), c.nodes[i].radius());
		}
		return c;
	}

	@Override
	public int layer() {
		return 2;
	}

	@Override
	public GenerateActorsChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed) {
		return GenerateActorsChunk.create(pos(), this, neighbours, worldSeed);
	}

	public <T extends RelocateNodesChunk> void cloneDataTo(T c) {
		c.relocatedNodes = relocatedNodes;
		super.cloneDataTo((GenerateNodesChunk) c);
	}

	public ActorClusterNode[] relocatedNodes() {
		return relocatedNodes;
	}

}
