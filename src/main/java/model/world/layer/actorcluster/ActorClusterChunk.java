package model.world.layer.actorcluster;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2i;
import model.actor.Actor;
import model.actor.resource.TreeActor;
import model.world.TileChunk;
import model.world.layer.finallayer.FinalLayerChunk;
import model.world.layer.relocatenodes.RelocateNodesChunk;

public class ActorClusterChunk extends RelocateNodesChunk {

	protected Actor[] actors;

	public ActorClusterChunk(Vector2i pos) {
		super(pos);
	}

	public static ActorClusterChunk create(Vector2i pos, RelocateNodesChunk rnc, TileChunk[][] neighbours, long worldSeed) {
		ActorClusterChunk c = new ActorClusterChunk(pos);
		c.nodes = rnc.nodes();
		c.relocatedNodes = rnc.relocatedNodes();

		List<Actor> actors = new ArrayList<>();
		for (ActorClusterNode node : c.relocatedNodes) {
			TreeActor treeActor = new TreeActor();
			treeActor.worldPos().setChunkPos(pos);
			treeActor.worldPos().setTilePos(node.tilePos());
			actors.add(treeActor);
		}

		c.actors = actors.toArray(new Actor[0]);
		return c;
	}

	@Override
	public int layer() {
		return 2;
	}

	@Override
	public FinalLayerChunk upgrade(TileChunk[][] neighbours, long worldSeed) {
		return FinalLayerChunk.create(pos(), this, neighbours, worldSeed);
	}

	public Actor[] actors() {
		return actors;
	}

}
