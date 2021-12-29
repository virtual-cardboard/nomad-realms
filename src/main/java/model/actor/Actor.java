package model.actor;

import static common.math.Vector2f.ORIGIN;
import static model.tile.Tile.tilePos;
import static model.tile.TileChunk.CHUNK_HEIGHT;
import static model.tile.TileChunk.CHUNK_WIDTH;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.NomadsSettings;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;
import model.GameState;
import model.card.GameCard;

public abstract class Actor extends GameObject {

	protected Vector2i chunkPos = new Vector2i();
	protected Vector2f pos = new Vector2f();
	protected Vector2f direction = new Vector2f(0, 1);
	protected Vector2f velocity = ORIGIN;

	@Override
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, GameCard> cards,
			Map<Vector2i, List<Actor>> chunkToActors) {
		actors.put(id, this);
		List<Actor> list = chunkToActors.get(chunkPos);
		if (list == null) {
			list = new ArrayList<>();
			chunkToActors.put(chunkPos, list);
		}
		list.add(this);
		super.addTo(actors, cardPlayers, cards, chunkToActors);
	}

	public <A extends Actor> A copyTo(A copy) {
		copy.id = id;
		copy.chunkPos = chunkPos;
		copy.pos = pos;
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
		Vector2i tilePos = tilePos(pos);
		if (tilePos.x < 0) {
			chunkPos = chunkPos.add(-1, 0);
			pos = pos.add(CHUNK_WIDTH, 0);
		} else if (tilePos.x > 15) {
			chunkPos = chunkPos.add(1, 0);
			pos = pos.add(-CHUNK_WIDTH, 0);
		}
		if (tilePos.y < 0) {
			chunkPos = chunkPos.add(0, -1);
			pos = pos.add(0, CHUNK_HEIGHT);
		} else if (tilePos.y > 15) {
			chunkPos = chunkPos.add(0, 1);
			pos = pos.add(0, -CHUNK_HEIGHT);
		}
		this.pos = pos;
	}

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
