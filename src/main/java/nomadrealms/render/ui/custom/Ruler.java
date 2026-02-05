package nomadrealms.render.ui.custom;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;

public class Ruler {

	private boolean show = false;

	public void render(RenderingEnvironment re) {
		if (!show) {
			return;
		}
		int screenHeight = (int) re.glContext.height();
		for (int i = 0; i < screenHeight; i += 10) {
			int width = 10;
			if (i % 100 == 0) {
				width = 30;
				re.textRenderer.render(width + 5, i - 10, String.valueOf(i), 0, re.font, 15, rgb(255, 255, 255));
			} else if (i % 50 == 0) {
				width = 20;
			}
			renderMark(re, 0, i, width, 2);
		}
	}

	private void renderMark(RenderingEnvironment re, float x, float y, float w, float h) {
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(255, 255, 255)))
				.set("transform", new Matrix4f(x, y, w, h, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
	}

	public void toggle() {
		show = !show;
	}

	public boolean show() {
		return show;
	}
}
