package com.nomadrealms.logic.game.world.area;

import com.badlogic.gdx.math.Vector2;

public class Tile {

	private Chunk chunk;
	private Vector2 pos;

	public Tile(Chunk chunk, int x, int y) {
		this.chunk = chunk;
		this.pos = new Vector2(x, y);
	}

	public Vector2 pos() {
		return pos;
	}

}
