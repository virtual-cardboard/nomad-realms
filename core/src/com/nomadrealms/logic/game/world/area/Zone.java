package com.nomadrealms.logic.game.world.area;

import com.badlogic.gdx.math.Vector2;

public class Zone {

	public static final int ZONE_SIZE = 16;

	private Region region;
	private Vector2 pos;

	private Chunk[][] chunks;

	public Zone(Vector2 pos) {
	}

	public Zone(Region region, int i, int j) {
		this.region = region;
		this.pos = new Vector2(i, j);
		chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y] = new Chunk(this, x, y);
			}
		}
	}

	public void render(Vector2 camera) {
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y].render(camera);
			}
		}
	}

}
