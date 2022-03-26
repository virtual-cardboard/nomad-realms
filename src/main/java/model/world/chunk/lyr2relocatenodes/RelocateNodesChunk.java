package model.world.chunk.lyr2relocatenodes;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2f;
import common.math.Vector2i;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.lyr1generatenodes.GenerateNodesChunk;
import model.world.chunk.lyr3generateActors.GenerateActorsChunk;
import model.world.chunk.lyr3generateActors.ActorClusterNode;

public class RelocateNodesChunk extends GenerateNodesChunk {

	protected ActorClusterNode[] relocatedNodes;

	public RelocateNodesChunk(Vector2i pos) {
		super(pos);
	}

	public static RelocateNodesChunk create(Vector2i pos, GenerateNodesChunk prev, AbstractTileChunk[][] neighbours, long worldSeed) {
		RelocateNodesChunk c = new RelocateNodesChunk(pos);

		prev.cloneDataTo(c);

		List<ActorClusterNode> allNodes = new ArrayList<>(27);
		for (AbstractTileChunk[] arr : neighbours) {
			for (AbstractTileChunk chunk : arr) {
				GenerateNodesChunk gnc = (GenerateNodesChunk) chunk;
				for (ActorClusterNode node : gnc.nodes()) {
					allNodes.add(node);
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
			ActorClusterNode n = allNodes.get(i);
			for (int j = 0; j < allNodes.size(); j++) {
				ActorClusterNode o = allNodes.get(j);
				if (n != o) {
					Vector2f nPos = n.pos();
					Vector2f oPos = o.pos();
					Vector2f sub = nPos.sub(oPos);
					float dist = sub.length();
					moveVecs[i] = moveVecs[i].add(sub.scale(o.radius() / dist));
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
