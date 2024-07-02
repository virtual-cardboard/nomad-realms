package nomadrealms.game.world.map.generation;

import common.math.Vector2i;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;

public interface MapGenerationStrategy {

	public Tile[][] generate(Chunk chunk, Vector2i coord);

	public Zone generateZone(World world, ZoneCoordinate zoneCoordinate);

}
