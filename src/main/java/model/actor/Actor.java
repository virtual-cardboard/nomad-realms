package model.actor;

import static common.math.Vector2f.ORIGIN;
import static model.world.Tile.tileCoords;
import static model.world.TileChunk.CHUNK_HEIGHT;
import static model.world.TileChunk.CHUNK_WIDTH;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;
import context.game.visuals.displayer.ActorDisplayer;
import model.state.GameState;

public abstract class Actor extends GameObject {

	protected Vector2i chunkPos = new Vector2i();
	protected Vector2f pos = new Vector2f();
	protected Vector2f direction = new Vector2f(0, 1);
	protected Vector2f velocity = ORIGIN;

	public Actor() {
	}

	public Actor(long id) {
		super(id);
	}

	@Override
	public void addTo(GameState state) {
		state.actors().put(id, this);
		List<Actor> list = state.chunkToActors().get(chunkPos);
		if (list == null) {
			list = new ArrayList<>();
			state.chunkToActors().put(chunkPos, list);
		}
		list.add(this);
	}

	public <A extends Actor> A copyTo(A copy) {
		copy.id = id;
		copy.chunkPos = chunkPos;
		copy.pos = pos;
		copy.direction = direction;
		copy.velocity = velocity;
		return copy;
	}

	public Vector2f screenPos(GameCamera camera, NomadsSettings s) {
		return relativePos(camera.chunkPos(), camera.pos()).scale(s.worldScale);
	}

	public Vector2f relativePos(Vector2i chunkPos, Vector2f pos) {
		Vector2i chunkDiff = this.chunkPos.sub(chunkPos);
		return this.pos.sub(pos).add(chunkDiff.x * CHUNK_WIDTH, chunkDiff.y * CHUNK_HEIGHT);
	}

	public void update(GameState state) {
		updatePos(pos.add(velocity));
	}

	public void updatePos(Vector2f pos) {
		Vector2i tilePos = tileCoords(pos);
		if (tilePos.x < 0) {
			setChunkPos(chunkPos.add(-1, 0));
			pos = pos.add(CHUNK_WIDTH, 0);
		} else if (tilePos.x > 15) {
			setChunkPos(chunkPos.add(1, 0));
			pos = pos.add(-CHUNK_WIDTH, 0);
		}
		if (tilePos.y < 0) {
			setChunkPos(chunkPos.add(0, -1));
			pos = pos.add(0, CHUNK_HEIGHT);
		} else if (tilePos.y > 15) {
			setChunkPos(chunkPos.add(0, 1));
			pos = pos.add(0, -CHUNK_HEIGHT);
		}
		this.pos = pos;
	}

	public abstract ActorDisplayer<?> displayer();

	public Vector2i chunkPos() {
		return chunkPos;
	}

	public void setChunkPos(Vector2i chunkPos) {
		this.chunkPos = chunkPos;
	}

	public Vector2f pos() {
		return pos;
	}

	public Vector2f direction() {
		return direction;
	}

	public void setDirection(Vector2f direction) {
		this.direction = direction;
	}

	public Vector2f velocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

}
