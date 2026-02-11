package nomadrealms.render.ui.content;

import static engine.common.colour.Colour.rgb;

import engine.common.math.Matrix4f;
import engine.context.input.Mouse;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.visuals.constraint.box.ConstraintBox;
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
		re.textureRenderer.render(
				re.imageMap.get("button"),
				new Matrix4f(constraintBox(), re.glContext)
		);

		// Render text
		re.textRenderer
				.alignCenterHorizontal()
				.alignCenterVertical()
				.render(
						constraintBox().center().x().get(), constraintBox().center().y().get(),
						text, constraintBox().w().get(), re.font, 30, rgb(255, 255, 255)
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