package nomadrealms.game.world.map.tile;

import common.math.Matrix4f;
import common.math.Vector2f;
import common.math.Vector2i;
import nomadrealms.game.event.Target;
import nomadrealms.game.item.WorldItem;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.HexagonVao;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

import java.util.ArrayList;
import java.util.List;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

public class Tile implements Target {

	/**
	 * The length of the side of the hexagon.
	 */
	public static final float SCALE = 40;
	private static final float ITEM_SCALE = SCALE * 0.6f;
	private final int x, y;
	protected int color = rgb(126, 200, 80);

	private List<WorldItem> items = new ArrayList<>();

	public Tile(int row, int col) {
		this.x = col;
		this.y = row;
	}

	public void render(RenderingEnvironment re) {
		Vector2f screenPosition = getScreenPosition();
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(color))
							.set("transform", new Matrix4f(
									screenPosition.x(), screenPosition.y(),
									SCALE * 2 * SIDE_LENGTH * 0.98f,
									SCALE * 2 * SIDE_LENGTH * 0.98f,
									re.glContext))
							.use(new DrawFunction()
									.vao(HexagonVao.instance())
									.glContext(re.glContext)
							);
					for (WorldItem item : items) {
						re.textureRenderer.render(
								re.imageMap.get(item.item().image()),
								screenPosition.x() - ITEM_SCALE * 0.5f, screenPosition.y() - ITEM_SCALE * 0.5f,
								ITEM_SCALE, ITEM_SCALE);
					}
				});
	}

	public Vector2f getScreenPosition() {
		float xIncrement = SCALE * SIDE_LENGTH * 1.5f;
		float yIncrement = SCALE * HEIGHT * 2;
		float yOffset = (x % 2 == 0) ? 0 : SCALE * HEIGHT;
		return new Vector2f(x * xIncrement + SCALE * SIDE_LENGTH, y * yIncrement + yOffset + SCALE * HEIGHT);
	}


	public static Vector2i screenToTile(Vector2f cursor) {
		float quarterWidth = SCALE * SIDE_LENGTH / 2;
		float threeQuartersWidth = SCALE * SIDE_LENGTH * 3 / 2;
		float halfHeight = SCALE * HEIGHT;
		float tileHeight = SCALE * HEIGHT * 2;

		int tileX = (int) (cursor.x() / threeQuartersWidth);
		int tileY;
		if (cursor.x() % threeQuartersWidth >= quarterWidth) {
			// In center rectangle of hexagon
			if (tileX % 2 == 0) {
				// Not shifted
				tileY = (int) (cursor.y() / tileHeight);
			} else {
				// Shifted
				tileY = (int) ((cursor.y() - halfHeight) / tileHeight);
			}
		} else {
			// Beside the zig-zag
			float xOffset;
			if ((int) (cursor.x() / threeQuartersWidth) % 2 == 0) {
				// Zig-zag starting from right side
				xOffset = quarterWidth * Math.abs(cursor.y() % tileHeight - halfHeight) / halfHeight;
			} else {
				// Zig-zag starting from left side
				xOffset = quarterWidth * Math.abs((cursor.y() + halfHeight) % tileHeight - halfHeight) / halfHeight;
			}
			if (cursor.x() % threeQuartersWidth <= xOffset) {
				// Left of zig-zag
				tileX--;
			}
			if (tileX % 2 == 0) {
				// Not shifted
				tileY = (int) (cursor.y() / tileHeight);
			} else {
				// Shifted
				tileY = (int) ((cursor.y() - halfHeight) / tileHeight);
			}
		}
		return new Vector2i(tileY, tileX);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public void addItem(WorldItem item) {
		items.add(item);
	}

	public void removeItem(WorldItem item) {
		items.remove(item);
	}


	@Override
	public String toString() {
		return "Tile{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
