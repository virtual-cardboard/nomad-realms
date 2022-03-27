package model.world.region;

import common.math.Vector2i;
import model.world.chunk.TileChunk;

public class Region {

	public static final int REGION_SIDE_LENGTH = 16;

	private Vector2i pos;
	private TileChunk[][] chunks = new TileChunk[REGION_SIDE_LENGTH][REGION_SIDE_LENGTH];

	public Region(Vector2i pos) {
		this.pos = pos;
	}

}
