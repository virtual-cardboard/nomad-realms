package com.nomadrealms.logic.game.world.area;

import static com.nomadrealms.rendering.basic.shape.HexagonShapeRenderer.renderHexagon;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.math.Vector2i;

public class Tile {

	public static final float TILE_RADIUS = 50;
	public static final float TILE_HORIZONTAL_SPACING = TILE_RADIUS * 1.732f;
	public static final float TILE_VERTICAL_SPACING = TILE_RADIUS * 1.5f;

	private Chunk chunk;
	private Vector2i index;

	public Tile(Chunk chunk, int x, int y) {
		this.chunk = chunk;
		this.index = new Vector2i(x, y);
	}

	private Vector2 indexPosition() {
		return new Vector2(index.x, index.y).scl(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING);
	}

	public void render(Vector2 camera) {
		Vector2 position = chunk.pos().add(indexPosition());
		position = new Vector2().add(indexPosition());
		Vector2 offset = position.sub(camera);
		if (index.y % 2 == 0) {
			offset.x += TILE_HORIZONTAL_SPACING / 2;
		}
		renderHexagon(offset.x, offset.y, TILE_RADIUS);
	}

}
