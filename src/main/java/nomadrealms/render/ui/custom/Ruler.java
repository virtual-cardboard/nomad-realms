package nomadrealms.render.ui.custom;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;

public class Ruler {

	private boolean show = false;
	private final Matrix4f transform = new Matrix4f();

	private static final int MINOR_TICK_INTERVAL = 10;
	private static final int MEDIUM_TICK_INTERVAL = 50;
	private static final int MAJOR_TICK_INTERVAL = 100;

	private static final int MINOR_TICK_WIDTH = 10;
	private static final int MEDIUM_TICK_WIDTH = 20;
	private static final int MAJOR_TICK_WIDTH = 30;

	private static final int TICK_HEIGHT = 2;
	private static final int TEXT_X_OFFSET = 5;
	private static final int TEXT_Y_OFFSET = -10;
	private static final int FONT_SIZE = 15;
	private static final int RULER_COLOR = rgb(255, 255, 255);

	public void render(RenderingEnvironment re) {
		if (!show) {
			return;
		}
		int screenHeight = (int) re.glContext.height();
		for (int i = 0; i < screenHeight; i += MINOR_TICK_INTERVAL) {
			int width;
			if (i % MAJOR_TICK_INTERVAL == 0) {
				width = MAJOR_TICK_WIDTH;
				re.textRenderer.render(width + TEXT_X_OFFSET, i + TEXT_Y_OFFSET, String.valueOf(i), 0, re.font, FONT_SIZE, RULER_COLOR);
			} else if (i % MEDIUM_TICK_INTERVAL == 0) {
				width = MEDIUM_TICK_WIDTH;
			} else {
				width = MINOR_TICK_WIDTH;
			}
			renderMark(re, 0, i, width, TICK_HEIGHT);
		}
	}

	private void renderMark(RenderingEnvironment re, float x, float y, float w, float h) {
		transform.setIdentity()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / re.glContext.width(), 1 / re.glContext.height())
				.translate(x, y)
				.scale(w, h);
		re.defaultShaderProgram
				.set("color", toRangedVector(RULER_COLOR))
				.set("transform", transform)
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
	}

	public void toggle() {
		show = !show;
	}

	public boolean show() {
		return show;
	}
}
