package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.*;
import static engine.common.java.JavaUtil.map;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.world.map.tile.factory.TileType.GRASS;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

import engine.common.java.Pair;
import engine.common.math.Matrix4f;
import engine.nengen.DrawBatch;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.serialization.Derializable;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import java.util.List;
import java.util.Map;
import java.util.Random;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.HexagonVao;

@Derializable
public class GrassTile extends Tile {

	private static final Map<Integer, ConstraintPair> GRASS_DECORATION_OFFSETS = map(
			new Pair<>(1, new ConstraintPair(absolute(-5), absolute(-1))),
			new Pair<>(2, new ConstraintPair(absolute(-8), absolute(-5))),
			new Pair<>(3, new ConstraintPair(absolute(0), absolute(0))),
			new Pair<>(4, new ConstraintPair(absolute(0), absolute(0))),
			new Pair<>(5, new ConstraintPair(absolute(-6), absolute(-2)))
	);

	private static final Map<Integer, ConstraintPair> GRASS_DECORATION_DIMENSIONS = map(
			new Pair<>(1, new ConstraintPair(absolute(15), absolute(8))),
			new Pair<>(2, new ConstraintPair(absolute(16), absolute(16))),
			new Pair<>(3, new ConstraintPair(absolute(8), absolute(7))),
			new Pair<>(4, new ConstraintPair(absolute(5), absolute(5))),
			new Pair<>(5, new ConstraintPair(absolute(5), absolute(5)))
	);

	private int grassType;

	/**
	 * No-arg constructor for serialization.
	 */
	protected GrassTile() {
	}

	public GrassTile(Chunk chunk, TileCoordinate coord) {
		this(chunk, coord, rgb(126, 200, 80));
	}

	public GrassTile(Chunk chunk, TileCoordinate coord, int rgb) {
		super(chunk, coord);
		int alt = rgb((int) (r(rgb) * 0.9f), (int) (g(rgb) * 0.9f), (int) (b(rgb) * 0.9f));
		this.color = (coord.x() + coord.y()) % 2 == 0 ? rgb : alt;
		this.grassType = ((coord.hashCode() & 0x7FFFFFFF) % 8) + 1;
	}

	private static final float[] RAW_OFFSETS_X = {0, -5, -8, 0, 0, -6};
	private static final float[] RAW_OFFSETS_Y = {0, -1, -5, 0, 0, -2};
	private static final float[] RAW_DIM_W = {0, 15, 16, 8, 5, 5};
	private static final float[] RAW_DIM_H = {0, 8, 16, 7, 5, 5};

	@Override
	public void collectDecorationData(DrawBatch batch, RenderingEnvironment re) {
		super.collectDecorationData(batch, re);
		float zoom = re.is.camera.zoom().get();
		if (zoom > 0.3f && grassType <= 5 && re.decorationSpriteSheet != null) {
			engine.visuals.lwjgl.render.CroppedTexture ct = re.decorationSpriteSheet.get("grass_" + grassType);
			if (ct != null) {
				Vector2f chunkWorldPos = chunk().pos().vector();
				float tileWorldCenterX = TILE_RADIUS * SIDE_LENGTH + coord().x() * TILE_HORIZONTAL_SPACING;
				float tileWorldCenterY = TILE_RADIUS * HEIGHT + coord().y() * TILE_VERTICAL_SPACING + ((coord().x() % 2 == 0) ? 0 : TILE_RADIUS * HEIGHT);

				float worldX = chunkWorldPos.x() + tileWorldCenterX;
				float worldY = chunkWorldPos.y() + tileWorldCenterY;

				float screenCenterX = (worldX - re.is.camera.position().x().get()) * zoom;
				float screenCenterY = (worldY - re.is.camera.position().y().get()) * zoom;

				float x = screenCenterX + RAW_OFFSETS_X[grassType] * zoom;
				float y = screenCenterY + RAW_OFFSETS_Y[grassType] * zoom;
				float w = RAW_DIM_W[grassType] * zoom;
				float h = RAW_DIM_H[grassType] * zoom;

				Matrix4f transform = new Matrix4f()
						.translate(-1, 1)
						.scale(2, -2)
						.scale(1f / re.glContext.width(), 1f / re.glContext.height())
						.translate(x, y)
						.scale(w, h);

				batch.add(transform, rgb(255, 255, 255), ct.cropBox());
			}
		}
	}

	@Override
	public void renderDecorations(RenderingEnvironment re) {
		super.renderDecorations(re);
	}

	@Override
	public void render(RenderingEnvironment re, Vector2f screenPosition, float scale, float radians) {
		float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
		float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;
		re.hexagonRenderer.renderTextured(
				new Matrix4f(
						screenPosition.x() - width * 0.5f, screenPosition.y() - height * 0.5f,
						width,
						height,
						re.glContext)
						.translate(0.5f, 0.5f)
						.rotate(radians, new Vector3f(0, 0, 1))
						.translate(-0.5f, -0.5f),
				width, height, color, re.imageMap.get("grass_texture"));
	}

	@Override
	public TileType type() {
		return GRASS;
	}

	@Override
	public List<Pair<String, String>> getTooltipEntries() {
		return asList(new Pair<>("Grass decoration type", valueOf(grassType)));
	}

}
