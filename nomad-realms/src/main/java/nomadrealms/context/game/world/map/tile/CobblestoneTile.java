package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.world.map.tile.factory.TileType.COBBLESTONE;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.serialization.Derializable;
import engine.visuals.lwjgl.render.CroppedTexture;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;

@Derializable
public class CobblestoneTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected CobblestoneTile() {
	}

	public CobblestoneTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		this.color = rgb(180, 180, 180);
	}

	@Override
	public void renderDecorations(RenderingEnvironment re) {
		super.renderDecorations(re);
		if (re.villageTexturesSpriteSheet != null) {
			CroppedTexture ct = re.villageTexturesSpriteSheet.get("cobblestone");
			if (ct != null) {
				Vector2f screenPosition = getScreenPosition(re).vector();
				float scale = re.is.camera.zoom().get();
				float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
				float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;
				re.hexagonRenderer.renderTextured(
						new Matrix4f(
								screenPosition.x() - width * 0.5f, screenPosition.y() - height * 0.5f,
								width,
								height,
								re.glContext),
						width, height, rgb(255, 255, 255), ct);
			}
		}
	}

	@Override
	public void render(RenderingEnvironment re, Vector2f screenPosition, float scale, float radians) {
		float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
		float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;
		if (re.villageTexturesSpriteSheet != null) {
			CroppedTexture ct = re.villageTexturesSpriteSheet.get("cobblestone");
			if (ct != null) {
				re.hexagonRenderer.renderTextured(
						new Matrix4f(
								screenPosition.x() - width * 0.5f, screenPosition.y() - height * 0.5f,
								width,
								height,
								re.glContext)
								.translate(0.5f, 0.5f)
								.rotate(radians, new Vector3f(0, 0, 1))
								.translate(-0.5f, -0.5f),
						width, height, rgb(255, 255, 255), ct);
				return;
			}
		}
		super.render(re, screenPosition, scale, radians);
	}

	@Override
	public TileType type() {
		return COBBLESTONE;
	}

}
