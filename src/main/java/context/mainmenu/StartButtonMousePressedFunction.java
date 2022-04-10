package context.mainmenu;

import java.util.function.Function;

import context.input.event.MousePressedInputEvent;
import context.visuals.gui.Gui;
import engine.common.event.GameEvent;
import engine.common.math.PosDim;
import engine.common.math.Vector2i;

public class StartButtonMousePressedFunction implements Function<MousePressedInputEvent, GameEvent> {

	private MainMenuInput input;
	private MainMenuVisuals visuals;

	public StartButtonMousePressedFunction(MainMenuVisuals visuals, MainMenuInput input) {
		this.visuals = visuals;
		this.input = input;
	}

	public boolean hoveringOver(Gui gui, Vector2i vector2i) {
		PosDim pd = gui.posdim();
		float cx = vector2i.x;
		float cy = vector2i.y;
		return pd.x <= cx && cx <= pd.x + pd.w && pd.y <= cy && cy <= pd.y + pd.h;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent t) {

		if (hoveringOver(visuals.startButton(), input.cursor().pos())) {
			input.transitionToConnect();
		}

		return null;
	}

}
