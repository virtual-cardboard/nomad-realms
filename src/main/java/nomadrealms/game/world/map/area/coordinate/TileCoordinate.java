package nomadrealms.game.world.map.area.coordinate;

import static common.math.MathUtil.posMod;
import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.chunkCoordinateOf;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import common.math.Vector2f;
import common.math.Vector2i;

public class TileCoordinate extends Coordinate {

	private final ChunkCoordinate chunk;

	public TileCoordinate(ChunkCoordinate chunk, int x, int y) {
		super(x, y);
		this.chunk = chunk;
	}

	public TileCoordinate(ChunkCoordinate chunk, Vector2i position) {
		this(chunk, position.x(), position.y());
	}

	// Up Left
	public TileCoordinate ul() {
		int tileY = posMod(x() % 2 == 0? y() - 1 : y(), CHUNK_SIZE);
		int tileX = posMod(x() - 1, CHUNK_SIZE);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == 0 ? chunkCoord.up() : chunkCoord;
		chunkCoord = x() == 0 ? chunkCoord.left() : chunkCoord;

		return new TileCoordinate(chunkCoord, tileX, tileY);
	}

	// Up Middle
	public TileCoordinate um() {
		int tileY = posMod(y() - 1, CHUNK_SIZE);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == 0 ? chunkCoord.up() : chunkCoord;

		return new TileCoordinate(chunkCoord, x(), tileY);
	}

	// Up Right
	public TileCoordinate ur() {
		int tileY = posMod(x() % 2 == 0? y() - 1 : y(), CHUNK_SIZE);
		int tileX = posMod(x() + 1, CHUNK_SIZE);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == 0 ? chunkCoord.up() : chunkCoord;
		chunkCoord = x() == CHUNK_SIZE - 1 ? chunkCoord.right() : chunkCoord;

		return new TileCoordinate(chunkCoord, tileX, tileY);
	}

	// Down Left
	public TileCoordinate dl() {
		int tileY = posMod(x() % 2 == 0? y() : y() + 1, CHUNK_SIZE);
		int tileX = posMod(x() - 1, CHUNK_SIZE);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == CHUNK_SIZE - 1 ? chunkCoord.down() : chunkCoord;
		chunkCoord = x() == 0 ? chunkCoord.left() : chunkCoord;

		return new TileCoordinate(chunkCoord, tileX, tileY);
	}

	// Down Middle
	public TileCoordinate dm() {
		int tileY = posMod(y() + 1, CHUNK_SIZE);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = y() == CHUNK_SIZE - 1 ? chunkCoord.down() : chunkCoord;

		return new TileCoordinate(chunkCoord, x(), tileY);
	}

	// Down Right
	public TileCoordinate dr() {
		int tileY = posMod(x() % 2 == 0? y() : y() + 1, CHUNK_SIZE);
		int tileX = posMod(x() + 1, CHUNK_SIZE);

		ChunkCoordinate chunkCoord = chunk;
		chunkCoord = (y() == CHUNK_SIZE - 1) ? chunkCoord.down() : chunkCoord;
		if (x() == CHUNK_SIZE - 1) {
			System.out.println("Down Right of " + this + " is in right chunk");
		}
		chunkCoord = (x() == CHUNK_SIZE - 1) ? chunkCoord.right() : chunkCoord;

		return new TileCoordinate(chunkCoord, tileX, tileY);
	}

	public static TileCoordinate tileCoordinateOf(Vector2f position) {
		ChunkCoordinate chunkCoord = chunkCoordinateOf(position);
		Vector2f offset = new Vector2f()
				.add(new Vector2f(chunkCoord.region().x(), chunkCoord.region().y()).scale(REGION_SIZE))
				.add(new Vector2f(chunkCoord.zone().x(), chunkCoord.zone().y()).scale(ZONE_SIZE))
				.add(new Vector2f(chunkCoord.x(), chunkCoord.y()).scale(CHUNK_SIZE))
				.scale(TILE_HORIZONTAL_SPACING, TILE_VERTICAL_SPACING)
				.sub(position).negate();

		float quarterWidth = TILE_RADIUS * SIDE_LENGTH / 2;
		float halfHeight = TILE_RADIUS * HEIGHT;

		int tileX = (int) (offset.x() / TILE_HORIZONTAL_SPACING);
		int tileY;
		if (offset.x() % TILE_HORIZONTAL_SPACING >= quarterWidth) {
			// In center rectangle of hexagon
			if (tileX % 2 == 0) {
				// Not shifted
				tileY = (int) (offset.y() / TILE_VERTICAL_SPACING);
			} else {
				// Shifted
				tileY = (int) ((offset.y() - halfHeight) / TILE_VERTICAL_SPACING);
			}
		} else {
			// Beside the zig-zag
			float xOffset;
			if ((int) (offset.x() / TILE_HORIZONTAL_SPACING) % 2 == 0) {
				// Zig-zag starting from right side
				xOffset = quarterWidth * Math.abs(offset.y() % TILE_VERTICAL_SPACING - halfHeight) / halfHeight;
			} else {
				// Zig-zag starting from left side
				xOffset = quarterWidth * Math.abs((offset.y() + halfHeight) % TILE_VERTICAL_SPACING - halfHeight) / halfHeight;
			}
			if (offset.x() % TILE_HORIZONTAL_SPACING <= xOffset) {
				// Left of zig-zag
				tileX--;
			}
			if (tileX % 2 == 0) {
				// Not shifted
				tileY = (int) (offset.y() / TILE_VERTICAL_SPACING);
			} else {
				// Shifted
				tileY = (int) ((offset.y() - halfHeight) / TILE_VERTICAL_SPACING);
			}
		}

		return new TileCoordinate(chunkCoord, tileX, tileY).normalize();
	}

	/**
	 * Run this if you want to fix coordinates that are out of bound instead of doing the tough math to prevent it
	 * from happening in the first place. Calling this is probably a bug waiting to happen.
	 */
	public TileCoordinate normalize() {
		int x = posMod(x(), CHUNK_SIZE);
		int y = posMod(y(), CHUNK_SIZE);
		ChunkCoordinate chunkCoord = chunk;
		if (x() < 0) {
			chunkCoord = chunkCoord.left();
		} else if (x() >= CHUNK_SIZE) {
			chunkCoord = chunkCoord.right();
		}
		if (y() < 0) {
			chunkCoord = chunkCoord.up();
		} else if (y() >= CHUNK_SIZE) {
			chunkCoord = chunkCoord.down();
		}
		return new TileCoordinate(chunkCoord, x, y);
	}

	@Override
	public RegionCoordinate region() {
		return chunk.region();
	}

	public ZoneCoordinate zone() {
		return chunk.zone();
	}

	public ChunkCoordinate chunk() {
		return chunk;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TileCoordinate) {
			TileCoordinate other = (TileCoordinate) o;
			return x() == other.x() && y() == other.y() && chunk.equals(other.chunk);
		}
		return false;
	}

	@Override
	public String toString() {
		return chunk.toString() + ".Tile(" + x() + "," + y() + ")";
	}

}
