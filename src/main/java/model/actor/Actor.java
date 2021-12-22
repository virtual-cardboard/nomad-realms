package model.actor;

import static model.tile.Tile.HALF_HEIGHT;
import static model.tile.Tile.QUARTER_WIDTH;
import static model.tile.Tile.THREE_QUARTERS_WIDTH;
import static model.tile.Tile.TILE_HEIGHT;
import static model.tile.TileChunk.CHUNK_PIXEL_HEIGHT;
import static model.tile.TileChunk.CHUNK_PIXEL_WIDTH;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;
import model.GameState;
import model.card.GameCard;

public abstract class Actor extends GameObject {

	protected Vector2i chunkPos = new Vector2i();
	protected Vector2f pos = new Vector2f();

	@Override
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, GameCard> cards, Map<Vector2i, List<Actor>> chunkToActors) {
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

	public Vector2f screenPos(GameCamera camera) {
		return relativePos(camera.chunkPos(), camera.pos());
	}

	public Vector2f relativePos(Vector2i chunkPos, Vector2f pos) {
		Vector2i chunkDiff = this.chunkPos.sub(chunkPos);
		return this.pos.sub(pos).add(chunkDiff.x * CHUNK_PIXEL_WIDTH, chunkDiff.y * CHUNK_PIXEL_HEIGHT);
	}

	public abstract void update(GameState state);

	public Vector2f pos() {
		return pos;
	}

	public void updatePos(Vector2f pos) {
		this.pos = pos;
		if (pos.x < QUARTER_WIDTH) {
			// Beside the zig-zag
			float xOffset;
			if ((int) (pos.x / THREE_QUARTERS_WIDTH) % 2 == 0) {
				// Zig-zag starting from right side
				xOffset = QUARTER_WIDTH * Math.abs(pos.y % TILE_HEIGHT - HALF_HEIGHT) / HALF_HEIGHT;
			} else {
				// Zig-zag starting from left side
				xOffset = QUARTER_WIDTH * Math.abs((pos.y + HALF_HEIGHT) % TILE_HEIGHT - HALF_HEIGHT) / HALF_HEIGHT;
			}
			if (pos.x > xOffset) {
				chunkPos = chunkPos.add(-1, 0);
				pos = pos.add(-CHUNK_PIXEL_WIDTH, 0);
			}
		} else if (pos.x > CHUNK_PIXEL_WIDTH) {
			// Beside the zig-zag
			float xOffset;
			if ((int) (pos.x / THREE_QUARTERS_WIDTH) % 2 == 0) {
				// Zig-zag starting from right side
				xOffset = QUARTER_WIDTH * Math.abs(pos.y % TILE_HEIGHT - HALF_HEIGHT) / HALF_HEIGHT;
			} else {
				// Zig-zag starting from left side
				xOffset = QUARTER_WIDTH * Math.abs((pos.y + HALF_HEIGHT) % TILE_HEIGHT - HALF_HEIGHT) / HALF_HEIGHT;
			}
			if (pos.x - CHUNK_PIXEL_WIDTH > xOffset) {
				chunkPos = chunkPos.add(1, 0);
				pos = pos.add(-CHUNK_PIXEL_WIDTH, 0);
			}
		}
	}

	public Vector2i chunkPos() {
		return chunkPos;
	}

	public void setChunkPos(Vector2i chunkPos) {
		this.chunkPos = chunkPos;
	}

}
