package nomadrealms.game.world.actor;

import static common.colour.Colour.rgb;
import static nomadrealms.game.world.map.tile.Tile.SCALE;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.game.world.map.tile.Tile;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class Nomad extends CardPlayer {

	private String name;
	private Tile tile;
	private int health;

	public Nomad(String name, Tile tile) {
		this.name = name;
		this.tile = tile;
		this.health = 10;
	}

	public void render(RenderingEnvironment re) {
		float scale = 0.6f * SCALE;
		DefaultFrameBuffer.instance().render(
				() -> {
					re.textureRenderer.render(
							re.imageMap.get("nomad"),
							tile.getScreenPosition().x() - 0.5f * scale,
							tile.getScreenPosition().y() - 0.7f * scale,
							scale, scale
					);
					re.textRenderer.render(
							tile.getScreenPosition().x(),
							tile.getScreenPosition().y() + 0.1f * scale,
							name,
							0,
							re.font,
							0.3f * scale,
							rgb(255, 255, 255)
					);
				}
		);
	}

	public void tile(Tile tile) {
		this.tile = tile;
	}

}
