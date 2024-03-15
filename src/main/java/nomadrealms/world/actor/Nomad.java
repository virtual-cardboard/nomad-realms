package nomadrealms.world.actor;

import common.math.Vector2f;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.world.map.Chunk;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class Nomad {

	private int x, y;
	private int health;

	public Nomad(int x, int y) {
		this.x = x;
		this.y = y;
		this.health = 10;
	}

	public void render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(
				() -> {
					re.textureRenderer.render(
							re.imageMap.get("nomad"),
							0, 0,
							100, 100
					);
				}
		);
	}

}
