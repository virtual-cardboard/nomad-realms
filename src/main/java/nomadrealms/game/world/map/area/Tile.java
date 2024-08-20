package nomadrealms.game.world.map.area;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import java.util.ArrayList;
import java.util.List;

import common.math.Matrix4f;
import common.math.Vector2f;
import nomadrealms.game.event.Target;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.HexagonVao;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class Tile implements Target {

	public static final float TILE_RADIUS = 40;
	private static final float ITEM_SIZE = TILE_RADIUS * 0.6f;
	public static final float TILE_HORIZONTAL_SPACING = TILE_RADIUS * SIDE_LENGTH * 1.5f;
	public static final float TILE_VERTICAL_SPACING = TILE_RADIUS * HEIGHT * 2;

	private Chunk chunk;
	private TileCoordinate coord;

	private final List<WorldItem> items = new ArrayList<>();
	private WorldItem buried;

	protected int color = rgb(126, 200, 80);

	public Tile(Chunk chunk, TileCoordinate coord) {
		this.chunk = chunk;
		this.coord = coord;
	}

	/**
	 * The position of thIS tile relative to the first tile of the chunk.
	 *
	 * @return
	 */
	private Vector2f indexPosition() {
		Vector2f toCenter = new Vector2f(TILE_RADIUS * SIDE_LENGTH, TILE_RADIUS * HEIGHT);
		Vector2f base = new Vector2f(coord.x() * TILE_HORIZONTAL_SPACING, coord.y() * TILE_VERTICAL_SPACING);
		Vector2f columnOffset = new Vector2f(0, (coord.x() % 2 == 0) ? 0 : TILE_RADIUS * HEIGHT);
		return toCenter.add(base).add(columnOffset);
	}

	/**
	 * Renders the tile at the given camera position.
	 * <p>
	 * Note for future self: it may be necessary to get the difference between the camera position and the tile position
	 * in a way that does not cause floating point errors, e.g. by gettng Coordinate difference first before converting
	 * to Vector2f.
	 *
	 * @param re rendering environment
	 */
	public void render(RenderingEnvironment re) {
		Vector2f position = chunk.pos().add(indexPosition());
		Vector2f screenPosition = position.sub(re.camera.position());
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(color))
							.set("transform", new Matrix4f(
									screenPosition.x(), screenPosition.y(),
									TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f,
									TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f,
									re.glContext))
							.use(new DrawFunction()
									.vao(HexagonVao.instance())
									.glContext(re.glContext)
							);
					for (WorldItem item : items) {
						re.textureRenderer.render(
								re.imageMap.get(item.item().image()),
								screenPosition.x() - ITEM_SIZE * 0.5f, screenPosition.y() - ITEM_SIZE * 0.5f,
								ITEM_SIZE, ITEM_SIZE);
					}
				});
	}

	public void addItem(WorldItem item) {
		items.add(item);
		item.tile(this);
	}

	public void removeItem(WorldItem item) {
		items.remove(item);
		item.tile(null);
	}

	public void buryItem(WorldItem item) {
		buried = item;
		item.tile(this);
		item.buried(true);
	}

	@Override
	public String toString() {
		return "Tile{" +
				"x=" + coord.x() + ", " +
				"y=" + coord.y() +
				'}';
	}

	public List<WorldItem> items() {
		return items;
	}

	public Chunk chunk() {
		return chunk;
	}

	public Tile ul(World world) {
		return chunk.getTile(coord.ul());
	}

	public Tile um(World world) {
		return chunk.getTile(coord.um());
	}

	public Tile ur(World world) {
		return chunk.getTile(coord.ur());
	}

	public Tile dl(World world) {
		return chunk.getTile(coord.dl());
	}

	public Tile dm(World world) {
		return chunk.getTile(coord.dm());
	}

	public Tile dr(World world) {
		return chunk.getTile(coord.dr());
	}

	public TileCoordinate coord() {
		return coord;
	}

	public Vector2f getScreenPosition(RenderingEnvironment re) {
		return chunk.pos().add(indexPosition()).sub(re.camera.position());
	}

}