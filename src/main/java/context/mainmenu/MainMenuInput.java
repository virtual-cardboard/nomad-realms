package context.mainmenu;

import context.GameContext;
import context.bootstrap.BootstrapGameData;
import context.bootstrap.BootstrapGameInput;
import context.bootstrap.BootstrapGameLogic;
import context.bootstrap.BootstrapGameVisuals;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.visuals.GameVisuals;

public class MainMenuInput extends GameInput {

	@Override
	protected void init() {
		MainMenuVisuals visuals = (MainMenuVisuals) context().visuals();
		addMousePressedFunction(new StartButtonMousePressedFunction(visuals, this));
	}

	public void transitionToConnect() {
		GameData data = new BootstrapGameData();
		GameInput input = new BootstrapGameInput();
		GameLogic logic = new BootstrapGameLogic();
		GameVisuals visuals = new BootstrapGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		context().transition(context);
	}

}
