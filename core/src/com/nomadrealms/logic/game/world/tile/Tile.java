package com.nomadrealms.logic.game.world.tile;

import com.badlogic.gdx.math.Vector2;

public class Tile {

	private Vector2 pos;

	public Tile(Vector2 pos) {
		this.pos = pos;
	}

	public Tile(float x, float y) {
		this.pos = new Vector2(x, y);
	}

	public Vector2 pos() {
		return pos;
	}

}
