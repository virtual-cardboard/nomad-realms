package nomadrealms.context.game.world.map.area;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.MIDDLE;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.nengen.DrawBatch;
import engine.serialization.Derializable;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.HasTooltip;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.UIContent;
import nomadrealms.render.ui.custom.tooltip.TooltipDeterminer;
import nomadrealms.render.vao.shape.HexagonVao;

/**
 * A tile is a hexagon-shaped smallest unit of the map. It contains items and can be walked on, as well as being
 * targetable by the player.
 */
@Derializable
public abstract class Tile implements Target, HasTooltip {

	public static final float TILE_RADIUS = 40;
	private static final float ITEM_SIZE = TILE_RADIUS * 0.6f;
	public static final float TILE_HORIZONTAL_SPACING = TILE_RADIUS * SIDE_LENGTH * 1.5f;
	public static final float TILE_VERTICAL_SPACING = TILE_RADIUS * HEIGHT * 2;

	private transient Chunk chunk;
	private TileCoordinate coord;

	private Actor actor;
	private final List<WorldItem> items = new ArrayList<>();
	private WorldItem buried;

	protected int color = rgb(126, 200, 80);

	/**
	 * No-arg constructor for serialization.
	 */
	protected Tile() {
	}

	public Tile(Chunk chunk, TileCoordinate coord) {
		this.chunk = chunk;
		this.coord = coord;
	}

	/**
	 * The position of thIS tile relative to the first tile of the chunk.
	 *
	 * @return
	 */
	public ConstraintPair indexPosition() {
		float x = TILE_RADIUS * SIDE_LENGTH + coord.x() * TILE_HORIZONTAL_SPACING;
		float y = TILE_RADIUS * HEIGHT + coord.y() * TILE_VERTICAL_SPACING + ((coord.x() % 2 == 0) ? 0 : TILE_RADIUS * HEIGHT);
		return new ConstraintPair(absolute(x), absolute(y));
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
		Vector2f screenPosition = getScreenPosition(re).vector();
		render(re, screenPosition, re.is.camera.zoom().get(), 0);
		renderDecorations(re);
	}

	public void collectData(DrawBatch batch, RenderingEnvironment re) {
		Vector2f screenPosition = getScreenPosition(re).vector();
		float scale = re.is.camera.zoom().get();
		float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
		float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;
		Matrix4f transform = new Matrix4f(
				screenPosition.x() - width * 0.5f, screenPosition.y() - height * 0.5f,
				width,
				height,
				re.glContext);
		batch.add(re.hexagonRenderer.padTransform(transform), color);
	}

	public void collectDecorationData(DrawBatch batch, RenderingEnvironment re) {
	}

	public void renderDecorations(RenderingEnvironment re) {
		Vector2f screenPosition = getScreenPosition(re).vector();
		float scale = re.is.camera.zoom().get();
		if (scale > 0.25) {
			for (WorldItem item : new ArrayList<>(items)) {
				re.textureRenderer.render(re.imageMap.get(item.item().image()), screenPosition.x() - ITEM_SIZE * 0.5f * scale,
						screenPosition.y() - ITEM_SIZE * 0.5f * scale, ITEM_SIZE * scale, ITEM_SIZE * scale);
			}
		}
	}

	/**
	 * Renders the tile at the given screen position with a rotation.
	 * <br>
	 * <br>
	 * Most of the time, you should use {@link #render(RenderingEnvironment)} instead.
	 *
	 * @param re             rendering environment
	 * @param screenPosition the screen position to render the tile at
	 * @param scale          the scale of the tile // TODO: not implemented
	 */
	public void render(RenderingEnvironment re, Vector2f screenPosition, float scale, float radians) {
		float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
		float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;
		re.hexagonRenderer.render(
				new Matrix4f(
						screenPosition.x() - width * 0.5f, screenPosition.y() - height * 0.5f,
						width,
						height,
						re.glContext)
						.translate(0.5f, 0.5f)
						.rotate(radians, new Vector3f(0, 0, 1))
						.translate(-0.5f, -0.5f),
				width, height, color, 0, 0);
	}

	public Actor actor() {
		return actor;
	}

	public void actor(Actor actor) {
		if (actor == null) {
			throw new IllegalArgumentException("Actor cannot be null. Use clearActor() instead.");
		}
		if (this.actor != null) {
			throw new IllegalStateException("Tile " + coord + " is already occupied by " + this.actor);
		}
		this.actor = actor;
		chunk.addActor(actor);
		actor.tile(this);
	}

	public void clearActor() {
		if (this.actor != null) {
			chunk.removeActor(this.actor);
		}
		this.actor = null;
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
		return "Tile{" + "x=" + coord.x() + ", " + "y=" + coord.y() + '}';
	}

	public List<WorldItem> items() {
		return items;
	}

	public WorldItem buried() {
		return buried;
	}

	public Chunk chunk() {
		return chunk;
	}

	public Zone zone() {
		return chunk.zone();
	}

	/**
	 * @param world the world to get the tile from
	 * @return the tile up-left of this tile
	 */
	public Tile ul(World world) {
		return world.getTile(coord.ul());
	}

	/**
	 * @param world the world to get the tile from
	 * @return the tile up-middle of this tile
	 */
	public Tile um(World world) {
		return world.getTile(coord.um());
	}

	/**
	 * @param world the world to get the tile from
	 * @return the tile up-right of this tile
	 */
	public Tile ur(World world) {
		return world.getTile(coord.ur());
	}

	/**
	 * @param world the world to get the tile from
	 * @return the tile down-left of this tile
	 */
	public Tile dl(World world) {
		return world.getTile(coord.dl());
	}

	/**
	 * @param world the world to get the tile from
	 * @return the tile down-middle of this tile
	 */
	public Tile dm(World world) {
		return world.getTile(coord.dm());
	}

	/**
	 * @param world the world to get the tile from
	 * @return the tile down-right of this tile
	 */
	public Tile dr(World world) {
		return world.getTile(coord.dr());
	}

	public TileCoordinate coord() {
		return coord;
	}

	public void copyStateTo(Tile newTile) {
		if (this.actor != null) {
			newTile.actor(this.actor);
		}
		for (WorldItem item : items) {
			newTile.addItem(item);
		}
		if (this.buried != null) {
			newTile.buryItem(this.buried);
		}
	}

	public ConstraintPair getScreenPosition(RenderingEnvironment re) {
		return chunk.pos().add(indexPosition()).sub(re.is.camera.position()).scale(re.is.camera.zoom());
	}

	public ConstraintPair pos() {
		float x = coord.x() * TILE_HORIZONTAL_SPACING + TILE_RADIUS * SIDE_LENGTH
				+ chunk.coord().x() * TILE_HORIZONTAL_SPACING * ChunkCoordinate.CHUNK_SIZE
				+ chunk.zone().coord().x() * TILE_HORIZONTAL_SPACING * ChunkCoordinate.CHUNK_SIZE * ZoneCoordinate.ZONE_SIZE
				+ chunk.zone().region().coord().x() * TILE_HORIZONTAL_SPACING * ChunkCoordinate.CHUNK_SIZE * ZoneCoordinate.ZONE_SIZE * RegionCoordinate.REGION_SIZE;

		float y = coord.y() * TILE_VERTICAL_SPACING + TILE_RADIUS * HEIGHT + ((coord.x() % 2 == 0) ? 0 : TILE_RADIUS * HEIGHT)
				+ chunk.coord().y() * TILE_VERTICAL_SPACING * ChunkCoordinate.CHUNK_SIZE
				+ chunk.zone().coord().y() * TILE_VERTICAL_SPACING * ChunkCoordinate.CHUNK_SIZE * ZoneCoordinate.ZONE_SIZE
				+ chunk.zone().region().coord().y() * TILE_VERTICAL_SPACING * ChunkCoordinate.CHUNK_SIZE * ZoneCoordinate.ZONE_SIZE * RegionCoordinate.REGION_SIZE;

		return new ConstraintPair(absolute(x), absolute(y));
	}

	public Appendage[] validAppendages() {
		return new Appendage[]{Appendage.LEG};
	}

	@Override
	public UIContent tooltip(TooltipDeterminer determiner) {
		return determiner.visit(this);
	}

	public abstract TileType type();

	@Override
	public Tile tile() {
		return this;
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex(Chunk chunk) {
		this.chunk = chunk;
		if (actor != null) {
			chunk.addActor(actor);
			actor.reindex(chunk.zone().region().world());
		}
		for (WorldItem item : items) {
			item.reindex(this);
		}
		if (buried != null) {
			buried.reindex(this);
		}
	}

}
