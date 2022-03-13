package model.world.chunk.relocatenodes;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2i;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.actorcluster.ActorClusterChunk;
import model.world.chunk.actorcluster.ActorClusterNode;
import model.world.chunk.generatenodes.GenerateNodesChunk;

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
		Vector2i[] moveVecs = new Vector2i[c.nodes.length];
		for (int i = 0; i < moveVecs.length; i++) {
			moveVecs[i] = new Vector2i();
		}

		// Edit movement vectors
		for (int i = 0; i < c.nodes.length; i++) {
			ActorClusterNode n = allNodes.get(i);
			for (int j = 0; j < allNodes.size(); j++) {
				ActorClusterNode o = allNodes.get(j);
				if (n != o) {
					Vector2i nPos = n.tilePos();
					Vector2i oPos = o.tilePos();
					Vector2i sub = oPos.sub(nPos);
					float dist = sub.length();
					moveVecs[i] = moveVecs[i].add(sub.scale(1 / dist));
				}
			}
		}

		// Create list of relocated nodes
		c.relocatedNodes = new ActorClusterNode[c.nodes.length];
		for (int i = 0; i < c.nodes.length; i++) {
			ActorClusterNode n = c.nodes[i];
			c.relocatedNodes[i] = new ActorClusterNode(n.tilePos().add(moveVecs[i]));
		}
		return c;
	}

	@Override
	public int layer() {
		return 2;
	}

	@Override
	public ActorClusterChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed) {
		return ActorClusterChunk.create(pos(), this, neighbours, worldSeed);
	}

	public <T extends RelocateNodesChunk> void cloneDataTo(T c) {
		c.relocatedNodes = relocatedNodes;
		super.cloneDataTo((GenerateNodesChunk) c);
	}

	public ActorClusterNode[] relocatedNodes() {
		return relocatedNodes;
	}

}
