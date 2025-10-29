package nomadrealms.render.ui.content;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.colour.Colour;
import engine.common.math.Matrix4f;
import engine.context.input.Mouse;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;

public class ButtonUIContent extends BasicUIContent {

	private final String text;
	private final Runnable onClick;

	public ButtonUIContent(UIContent parent, String text, ConstraintBox constraintBox, Runnable onClick) {
		super(parent, constraintBox);
		this.text = text;
		this.onClick = onClick;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		// Render background
		DefaultFrameBuffer.instance().render(() -> {
			re.defaultShaderProgram
					.set("color", toRangedVector(rgba(100, 0, 0, 60)))
					.set("transform", new Matrix4f(constraintBox(), re.glContext))
					.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		});

		// Render text
		re.textRenderer.alignCenterHorizontal();
		re.textRenderer.alignCenterVertical();
		re.textRenderer.render(
				constraintBox().center().x().get(),
				constraintBox().center().y().get(),
				text,
				constraintBox().w().get() - 20,
				re.font,
				20,
				Colour.rgb(255, 255, 255)
		);
	}

	public boolean isMouseOver(Mouse mouse) {
		return constraintBox().contains(mouse.coordinate().vector());
	}

	public void click() {
		onClick.run();
	}

}