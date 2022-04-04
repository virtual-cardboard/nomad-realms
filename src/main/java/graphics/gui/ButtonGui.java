package graphics.gui;

import java.util.function.Consumer;

import common.math.Vector2i;
import context.visuals.gui.Gui;
import context.visuals.gui.traits.HasClickEffect;

public abstract class ButtonGui extends Gui implements HasClickEffect {

	private boolean pressed;
	private Consumer<Vector2i> onClick;

	public ButtonGui(Runnable onClick) {
		this(v -> onClick.run());
	}

	public ButtonGui(Consumer<Vector2i> onClick) {
		this.onClick = onClick;
	}

	@Override
	public boolean isPressed() {
		return pressed;
	}

	@Override
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	@Override
	public Consumer<Vector2i> getPressEffect() {
		return null;
	}

	@Override
	public Consumer<Vector2i> getReleaseEffect() {
		return onClick;
	}

}
