package nomadrealms.game.world.map.area;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import common.math.Vector2f;
import common.math.Vector2i;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;

public class Region {

	private long timestamp;
	private final MapGenerationStrategy strategy;
	private final World world;

	private final RegionCoordinate coord;

	private final Zone[][] zones;

	public Region(MapGenerationStrategy strategy, World world, RegionCoordinate coord) {
		this.strategy = strategy;
		this.world = world;
		this.coord = coord;
		zones = new Zone[REGION_SIZE][REGION_SIZE];
	}

	public void render(RenderingEnvironment re) {
		Vector2i zoneIndex = zoneIndexOf(re.camera.position());
		if (zones[zoneIndex.x()][zoneIndex.y()] == null) {
			zones[zoneIndex.x()][zoneIndex.y()] = strategy.generateZone(world, new ZoneCoordinate(coord, zoneIndex.x(), zoneIndex.y()));
		}
		zones[zoneIndex.x()][zoneIndex.y()].render(re);
	}

	private Vector2i zoneIndexOf(Vector2f position) {
		float tileToZone = ZONE_SIZE * CHUNK_SIZE;
		float tileToRegion = REGION_SIZE * tileToZone;
		Vector2f offset = new Vector2f(position.x() - tileToRegion * coord.x(), position.y() - tileToRegion * coord.y());
		return new Vector2i((int) (offset.x() / tileToZone), (int) (offset.y() / tileToZone));
	}

	private Vector2f indexPosition() {
		return new Vector2f(coord.x() * TILE_HORIZONTAL_SPACING, coord.y() * TILE_VERTICAL_SPACING).scale(REGION_SIZE * ZONE_SIZE * CHUNK_SIZE);
	}

	public Vector2f pos() {
		return indexPosition();
	}

	public RegionCoordinate coord() {
		return coord;
	}

	public Tile getTile(TileCoordinate tile) {
		assert tile.region().equals(coord);
		if(zones[tile.zone().x()][tile.zone().y()] == null) {
			zones[tile.zone().x()][tile.zone().y()] = strategy.generateZone(world, new ZoneCoordinate(coord, tile.zone().x(), tile.zone().y()));
		}
		return zones[tile.zone().x()][tile.zone().y()].getTile(tile);
	}

}
