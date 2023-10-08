package com.nomadrealms.logic.game.world.area;

import com.badlogic.gdx.math.Vector2;

public class Region {

	private static final int REGION_SIZE = 16;

	private Vector2 pos;

	private Zone[][] zones;

	public Region() {
		zones = new Zone[REGION_SIZE][REGION_SIZE];
		for (int x = 0; x < REGION_SIZE; x++) {
			for (int y = 0; y < REGION_SIZE; y++) {
				zones[x][y] = new Zone();
			}
		}
	}

}
