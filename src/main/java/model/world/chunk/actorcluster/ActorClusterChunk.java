package model.world.chunk.actorcluster;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2i;
import model.actor.Actor;
import model.actor.resource.TreeActor;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.TileChunk;
import model.world.chunk.relocatenodes.RelocateNodesChunk;

public class ActorClusterChunk extends RelocateNodesChunk {

	protected Actor[] actors;

	public ActorClusterChunk(Vector2i pos) {
		super(pos);
	}

	public static ActorClusterChunk create(Vector2i pos, RelocateNodesChunk prev, AbstractTileChunk[][] neighbours, long worldSeed) {
		ActorClusterChunk c = new ActorClusterChunk(pos);

		prev.cloneDataTo(c);

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
		return 3;
	}

	@Override
	public TileChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed) {
		return TileChunk.create(pos(), this, neighbours, worldSeed);
	}

	public <T extends ActorClusterChunk> void cloneDataTo(T c) {
		c.actors = actors;
		super.cloneDataTo((RelocateNodesChunk) c);
	}

	public Actor[] actors() {
		return actors;
	}

}