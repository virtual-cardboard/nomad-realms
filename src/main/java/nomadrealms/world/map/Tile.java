package nomadrealms.world.map;

import static common.colour.Colour.rgb;
import static common.colour.Colour.rgba;
import static common.colour.Colour.toRangedVector;
import static common.colour.Colour.toVector;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import common.math.Matrix4f;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.HexagonVao;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class Tile {

	private int x, y;
	private int color = rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void render(RenderingEnvironment re) {
		float scale = 40;
		float xIncrement = scale * SIDE_LENGTH * 1.5f;
		float yIncrement = scale * HEIGHT * 2;
		float yOffset = (x % 2 == 0) ? 0 : scale * HEIGHT;
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(color))
							.set("transform", new Matrix4f(
									x * xIncrement,
									y * yIncrement + yOffset,
									scale * 2 * SIDE_LENGTH,
									scale * 2 * SIDE_LENGTH,
									re.glContext))
							.use(new DrawFunction()
									.vao(HexagonVao.instance())
									.glContext(re.glContext)
							);
				});
	}

}
