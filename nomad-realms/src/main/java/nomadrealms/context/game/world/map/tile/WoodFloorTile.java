package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.world.map.tile.factory.TileType.WOOD_FLOOR;
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
public class WoodFloorTile extends Tile {

	/**
	 * No-arg constructor for serialization.
	 */
	protected WoodFloorTile() {
	}

	public WoodFloorTile(Chunk chunk, TileCoordinate coord) {
		super(chunk, coord);
		int color1 = rgb(150, 110, 70);
		int color2 = rgb(135, 95, 55);
		this.color = (coord.x() + coord.y()) % 2 == 0 ? color1 : color2;
	}

	@Override
	public void render(RenderingEnvironment re, Vector2f screenPosition, float scale, float radians) {
		float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
		float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;
		Matrix4f matrix = new Matrix4f(
				screenPosition.x() - width * 0.5f, screenPosition.y() - height * 0.5f,
				width,
				height,
				re.glContext)
				.translate(0.5f, 0.5f)
				.rotate(radians, new Vector3f(0, 0, 1))
				.translate(-0.5f, -0.5f);
		CroppedTexture texture = re.villageTexturesSpriteSheet != null ? re.villageTexturesSpriteSheet.get("wood_floor") : null;
		if (texture != null) {
			re.hexagonRenderer.renderTextured(matrix, width, height, rgb(255, 255, 255), texture);
		} else {
			re.hexagonRenderer.render(matrix, width, height, color, 0, 0);
		}
	}

	@Override
	public TileType type() {
		return WOOD_FLOOR;
	}

}
