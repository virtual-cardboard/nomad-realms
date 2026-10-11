package nomadrealms.context.game.card.query.tile;

import static java.util.Collections.emptyList;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.context.game.world.map.area.coordinate.TileCoordinate.tileCoordinateOf;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Vector2f;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.event.game.effect.EffectContext;

/**
 * A query that selects all hexagonal tiles in the world that intersect with the outline
 * of an axis-aligned rectangle defined by its center position, total width, total height,
 * and inner outline size (thickness).
 */
public class RectangleTileQuery implements Query<Tile> {

	private final Vector2f center;
	private final float outlineSize;
	private final float width;
	private final float height;

	public RectangleTileQuery(Vector2f center, float outlineSize, float width, float height) {
		this.center = center;
		this.outlineSize = outlineSize;
		this.width = width;
		this.height = height;
	}

	public RectangleTileQuery(Vector2f center, float outlineSize, Vector2f size) {
		this(center, outlineSize, size.x(), size.y());
	}

	@Override
	public List<Tile> find(EffectContext context) {
		if (context == null || context.world() == null) {
			return emptyList();
		}
		World world = context.world();

		float halfW = width * 0.5f;
		float halfH = height * 0.5f;

		// Calculate world space bounds for candidate search area.
		// Tile bounding radius is TILE_RADIUS (or sqrt((SIDE_LENGTH*TILE_RADIUS)^2 + (HEIGHT*TILE_RADIUS)^2)).
		// Max radius from tile center to vertex is max(SIDE_LENGTH, sqrt(0.25^2 + HEIGHT^2)) * TILE_RADIUS = TILE_RADIUS * 0.5.
		float maxTileRadius = TILE_RADIUS;

		float minX = center.x() - halfW - maxTileRadius;
		float maxX = center.x() + halfW + maxTileRadius;
		float minY = center.y() - halfH - maxTileRadius;
		float maxY = center.y() + halfH + maxTileRadius;

		TileCoordinate topLeftCoord = tileCoordinateOf(new Vector2f(minX, minY));
		TileCoordinate bottomRightCoord = tileCoordinateOf(new Vector2f(maxX, maxY));

		int minTileDist = topLeftCoord.distanceTo(bottomRightCoord);
		int searchRadius = Math.max(1, (minTileDist / 2) + 2);

		Tile centerTile = world.getTile(tileCoordinateOf(center));
		if (centerTile == null) {
			return emptyList();
		}

		// Outer rectangle bounds
		float outerMinX = center.x() - halfW;
		float outerMaxX = center.x() + halfW;
		float outerMinY = center.y() - halfH;
		float outerMaxY = center.y() + halfH;

		// Inner cutout bounds (clamped if outlineSize >= half width or half height)
		float effectiveOutlineX = Math.min(outlineSize, halfW);
		float effectiveOutlineY = Math.min(outlineSize, halfH);
		float innerMinX = outerMinX + effectiveOutlineX;
		float innerMaxX = outerMaxX - effectiveOutlineX;
		float innerMinY = outerMinY + effectiveOutlineY;
		float innerMaxY = outerMaxY - effectiveOutlineY;

		// The outline ring consists of 4 axis-aligned sub-rectangles:
		// Top rect: [outerMinX, outerMaxX] x [outerMinY, innerMinY]
		// Bottom rect: [outerMinX, outerMaxX] x [innerMaxY, outerMaxY]
		// Left rect: [outerMinX, innerMinX] x [innerMinY, innerMaxY]
		// Right rect: [innerMaxX, outerMaxX] x [innerMinY, innerMaxY]
		RectangleRegion[] outlineSubRects = new RectangleRegion[] {
				new RectangleRegion(outerMinX, outerMaxX, outerMinY, innerMinY),
				new RectangleRegion(outerMinX, outerMaxX, innerMaxY, outerMaxY),
				new RectangleRegion(outerMinX, innerMinX, innerMinY, innerMaxY),
				new RectangleRegion(innerMaxX, outerMaxX, innerMinY, innerMaxY)
		};

		// Query candidate tiles using TilesInRadiusQuery centered at centerTile
		List<Tile> candidates = new TilesInRadiusQuery(searchRadius + 1).find(
				new EffectContext().world(world).source(context.source()).target(centerTile)
		);

		List<Tile> result = new ArrayList<>();
		for (Tile tile : candidates) {
			if (tile == null) {
				continue;
			}
			Vector2f tilePos = tile.pos().vector();
			Vector2f[] hexVertices = getHexagonVertices(tilePos);

			if (intersectsOutline(hexVertices, outlineSubRects)) {
				result.add(tile);
			}
		}

		return result;
	}

	private static Vector2f[] getHexagonVertices(Vector2f center) {
		float sl = SIDE_LENGTH * TILE_RADIUS;
		float h = HEIGHT * TILE_RADIUS;
		return new Vector2f[] {
				new Vector2f(center.x() - sl, center.y()),
				new Vector2f(center.x() - sl * 0.5f, center.y() - h),
				new Vector2f(center.x() + sl * 0.5f, center.y() - h),
				new Vector2f(center.x() + sl, center.y()),
				new Vector2f(center.x() + sl * 0.5f, center.y() + h),
				new Vector2f(center.x() - sl * 0.5f, center.y() + h)
		};
	}

	private static boolean intersectsOutline(Vector2f[] hexVertices, RectangleRegion[] outlineSubRects) {
		for (RectangleRegion rect : outlineSubRects) {
			if (rect.minX >= rect.maxX || rect.minY >= rect.maxY) {
				continue;
			}
			if (polygonIntersectsAABB(hexVertices, rect.minX, rect.maxX, rect.minY, rect.maxY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Separating Axis Theorem (SAT) check for convex polygon (hexagon) and Axis-Aligned Bounding Box (AABB).
	 */
	private static boolean polygonIntersectsAABB(Vector2f[] polygon, float minX, float maxX, float minY, float maxY) {
		Vector2f[] aabbVerts = new Vector2f[] {
				new Vector2f(minX, minY),
				new Vector2f(maxX, minY),
				new Vector2f(maxX, maxY),
				new Vector2f(minX, maxY)
		};

		// Test axes from AABB normals: (1, 0) and (0, 1)
		float polyMinX = Float.POSITIVE_INFINITY, polyMaxX = Float.NEGATIVE_INFINITY;
		float polyMinY = Float.POSITIVE_INFINITY, polyMaxY = Float.NEGATIVE_INFINITY;
		for (Vector2f p : polygon) {
			if (p.x() < polyMinX) polyMinX = p.x();
			if (p.x() > polyMaxX) polyMaxX = p.x();
			if (p.y() < polyMinY) polyMinY = p.y();
			if (p.y() > polyMaxY) polyMaxY = p.y();
		}

		if (polyMaxX < minX || polyMinX > maxX) return false;
		if (polyMaxY < minY || polyMinY > maxY) return false;

		// Test axes perpendicular to hexagon edges
		int n = polygon.length;
		for (int i = 0; i < n; i++) {
			Vector2f p1 = polygon[i];
			Vector2f p2 = polygon[(i + 1) % n];
			Vector2f edge = p2.sub(p1);
			Vector2f axis = new Vector2f(-edge.y(), edge.x()); // Normal to edge

			// Project polygon onto axis
			float pMin = Float.POSITIVE_INFINITY, pMax = Float.NEGATIVE_INFINITY;
			for (Vector2f p : polygon) {
				float proj = p.dot(axis);
				if (proj < pMin) pMin = proj;
				if (proj > pMax) pMax = proj;
			}

			// Project AABB onto axis
			float rMin = Float.POSITIVE_INFINITY, rMax = Float.NEGATIVE_INFINITY;
			for (Vector2f v : aabbVerts) {
				float proj = v.dot(axis);
				if (proj < rMin) rMin = proj;
				if (proj > rMax) rMax = proj;
			}

			if (pMax < rMin || pMin > rMax) return false;
		}

		return true;
	}

	public Vector2f center() {
		return center;
	}

	public float outlineSize() {
		return outlineSize;
	}

	public float width() {
		return width;
	}

	public float height() {
		return height;
	}

	private static class RectangleRegion {
		final float minX, maxX, minY, maxY;

		RectangleRegion(float minX, float maxX, float minY, float maxY) {
			this.minX = minX;
			this.maxX = maxX;
			this.minY = minY;
			this.maxY = maxY;
		}
	}

}
