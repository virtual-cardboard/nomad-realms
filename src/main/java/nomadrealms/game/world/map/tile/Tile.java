package nomadrealms.game.world.map.tile;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import common.math.Matrix4f;
import common.math.Vector2f;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.HexagonVao;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class Tile {

	public static final float SCALE = 40;
	private int x, y;
	protected int color = rgb(126, 200, 80);
//	private int color = rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));

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
				});
	}

	public Vector2f getScreenPosition() {
		float xIncrement = SCALE * SIDE_LENGTH * 1.5f;
		float yIncrement = SCALE * HEIGHT * 2;
		float yOffset = (x % 2 == 0) ? 0 : SCALE * HEIGHT;
		return new Vector2f(x * xIncrement, y * yIncrement + yOffset);
	}

}
