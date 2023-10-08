package com.nomadrealms.logic.game.world.area;

import com.badlogic.gdx.math.Vector2;

public class Zone {

	private static final int ZONE_SIZE = 16;

	private Vector2 pos;

	private Chunk[][] chunks;

	public Zone() {
		chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y] = new Chunk();
			}
		}
	}

}
