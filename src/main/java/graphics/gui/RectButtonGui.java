package graphics.gui;

import java.util.function.Supplier;

import common.event.GameEvent;
import context.GLContext;
import context.data.GameData;
import context.visuals.builtin.RectangleRenderer;

public class RectButtonGui extends ButtonGui {

	private RectangleRenderer rectangleRenderer;
	private int colour = -1;

	public RectButtonGui(RectangleRenderer renderer) {
		this.rectangleRenderer = renderer;
	}

	public RectButtonGui(RectangleRenderer renderer, Supplier<GameEvent> onClick) {
		super(onClick);
		rectangleRenderer = renderer;
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		rectangleRenderer.render(x, y, width, height, colour);
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

}
