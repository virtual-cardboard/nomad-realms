package graphics.gui;

import java.util.function.Supplier;

import context.visuals.gui.Gui;
import context.visuals.gui.traits.HasClickEffect;
import engine.common.event.GameEvent;

public abstract class ButtonGui extends Gui implements HasClickEffect {

	private boolean pressed;
	private Supplier<GameEvent> onClick;

	public ButtonGui() {
	}

	public ButtonGui(Supplier<GameEvent> onClick) {
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
	public Supplier<GameEvent> getPressEffect() {
		return null;
	}

	@Override
	public Supplier<GameEvent> getReleaseEffect() {
		return onClick;
	}

	public void setOnClick(Supplier<GameEvent> onClick) {
		this.onClick = onClick;
	}

}
