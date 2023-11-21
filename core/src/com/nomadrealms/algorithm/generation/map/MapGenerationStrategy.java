package com.nomadrealms.algorithm.generation.map;

import com.nomadrealms.logic.game.world.World;
import com.nomadrealms.logic.game.world.area.Zone;
import com.nomadrealms.math.coordinate.map.ZoneCoordinate;

public interface MapGenerationStrategy {

	Zone generateZone(World world, ZoneCoordinate coord);

}
