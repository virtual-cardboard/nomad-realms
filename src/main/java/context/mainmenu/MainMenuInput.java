package context.mainmenu;

import context.input.GameInput;

public class MainMenuInput extends GameInput {

	@Override
	protected void init() {
		MainMenuVisuals visuals = (MainMenuVisuals) context().visuals();
		addMousePressedFunction(new StartButtonMousePressedFunction(visuals, this));
	}

}
