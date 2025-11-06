package nomadrealms.render.ui.content;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.colour.Colour;
import engine.common.math.Matrix4f;
import engine.context.input.Mouse;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;

public class ButtonUIContent extends BasicUIContent {

	private String text;
	private Runnable onClick;

	private boolean isHovered = false;

	public ButtonUIContent(UIContent parent, String text, ConstraintBox constraintBox, Runnable onClick) {
		super(parent, constraintBox);
		this.text = text;
		this.onClick = onClick;
	}

	public ButtonUIContent(UIContent parent, String text, ConstraintBox constraintBox, Runnable onClick,
						   InputCallbackRegistry registry) {
		this(parent, text, constraintBox, onClick);
		registerCallbacks(registry);
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

	public void setCallbacks(Runnable onClick) {
		this.onClick = onClick;
	}

	public void input(MousePressedInputEvent event) {
		if (isMouseOver(event.mouse())) {
			onClick.run();
		}
	}

	public void input(MouseMovedInputEvent event) {
		if (isMouseOver(event.mouse())) {
			isHovered = true;
		} else {
			isHovered = false;
		}
	}

	public void input(MouseReleasedInputEvent event) {

	}

	private boolean isMouseOver(Mouse mouse) {
		return constraintBox().contains(mouse.coordinate().vector());
	}


	public void registerCallbacks(InputCallbackRegistry registry) {
		registry.registerOnPress(this::input);
		registry.registerOnDrag(this::input);
		registry.registerOnDrop(this::input);
	}
}