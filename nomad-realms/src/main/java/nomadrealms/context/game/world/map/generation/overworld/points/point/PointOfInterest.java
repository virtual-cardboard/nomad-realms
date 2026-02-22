package nomadrealms.context.game.world.map.generation.overworld.points.point;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static nomadrealms.context.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.overworld.points.PointsGenerationStep;
import nomadrealms.render.RenderingEnvironment;

/**
 * Represents a point of interest in the zone.
 *
 * @author Lunkle
 * @see PointsGenerationStep
 */
public class PointOfInterest {

	private Vector2f position;
	private POIType type;
	private int rarity;
	private int size;

	/**
	 * No-args constructor for serialization.
	 */
	public PointOfInterest() {
	}

	public PointOfInterest(Vector2f position, POIType type, int rarity, int size) {
		this.position = position;
		this.type = type;
		this.rarity = rarity;
		this.size = size;
	}

	/**
	 * The position of this point of interest relative to the top left corner of the zone. x and y are in the range [0,
	 * 1].
	 */
	public Vector2f position() {
		return position;
	}

	public POIType type() {
		return type;
	}

	/**
	 * The rarity of this point of interest.
	 *
	 * @return an integer representing the rarity level.
	 */
	public int rarity() {
		return rarity;
	}

	/**
	 * The size of this point of interest.
	 *
	 * @return an integer representing the size of the point.
	 */
	public int size() {
		return size;
	}

	public void render(Zone zone, RenderingEnvironment re) {
		float zoneSizeHorizontal = ZONE_SIZE * CHUNK_SIZE * TILE_HORIZONTAL_SPACING;
		float zoneSizeVertical = ZONE_SIZE * CHUNK_SIZE * TILE_VERTICAL_SPACING;
		float zoom = re.camera.zoom().get();
		float size = 100 * zoom;
		Vector2f worldPos = position.scale(zoneSizeHorizontal, zoneSizeVertical).add(zone.pos().vector());
		Vector2f screenPos = worldPos.sub(re.camera.position().vector()).scale(zoom).sub(size / 2, size / 2);
		re.circleShaderProgram
				.set("color", toRangedVector(rgb(100, 0, 0)))
				.set("transform", new Matrix4f(screenPos.x(), screenPos.y(), size, size, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
	}
}
