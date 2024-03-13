package nomadrealms.world.map;

import static common.colour.Colour.rgb;
import static common.colour.Colour.rgba;
import static common.colour.Colour.toRangedVector;
import static common.colour.Colour.toVector;

import common.math.Matrix4f;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.HexagonVao;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.lwjgl.GLContext;
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
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(color))
							.set("transform", new Matrix4f(x * 32, y * 32, 32, 32, re.glContext))
							.use(new DrawFunction()
									.vao(HexagonVao.instance())
									.glContext(re.glContext)
							);
				});
	}

}
