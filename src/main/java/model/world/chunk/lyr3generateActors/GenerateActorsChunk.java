package model.world.chunk.lyr3generateActors;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2f;
import common.math.Vector2i;
import model.actor.Actor;
import model.actor.resource.TreeActor;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.TileChunk;
import model.world.chunk.lyr2relocatenodes.RelocateNodesChunk;

public class GenerateActorsChunk extends RelocateNodesChunk {

	protected Actor[] actors;

	public GenerateActorsChunk(Vector2i pos) {
		super(pos);
	}

	public static GenerateActorsChunk create(Vector2i pos, RelocateNodesChunk prev, AbstractTileChunk[][] neighbours, long worldSeed) {
		GenerateActorsChunk c = new GenerateActorsChunk(pos);

		prev.cloneDataTo(c);

		List<Actor> actors = new ArrayList<>();
		for (ActorClusterNode node : c.relocatedNodes) {
			TreeActor treeActor = new TreeActor();
			treeActor.worldPos().setChunkPos(pos);
			Vector2f p = node.pos();
			Vector2i tilePos = new Vector2i((int) p.x, (int) p.y);
			treeActor.worldPos().setTilePos(tilePos);
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

	public <T extends GenerateActorsChunk> void cloneDataTo(T c) {
		c.actors = actors;
		super.cloneDataTo((RelocateNodesChunk) c);
	}

	public Actor[] actors() {
		return actors;
	}

}