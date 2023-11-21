package com.nomadrealms.algorithm.generation.map;

import static com.nomadrealms.math.coordinate.map.ZoneCoordinate.ZONE_SIZE;

import com.nomadrealms.logic.game.world.World;
import com.nomadrealms.logic.game.world.area.Chunk;
import com.nomadrealms.logic.game.world.area.Region;
import com.nomadrealms.logic.game.world.area.Zone;
import com.nomadrealms.math.coordinate.map.ZoneCoordinate;

public class PlaceholderMapGenerationStrategy implements MapGenerationStrategy {

	@Override
	public Zone generateZone(World world, ZoneCoordinate coord) {
		Region region = world.getRegion(coord.region());
		Zone zone = new Zone(region, coord);
		Chunk[][] chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				chunks[x][y] = new Chunk(zone, x, y);
			}
		}
		return zone;
	}

}
