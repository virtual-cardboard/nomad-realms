package context.loading;

import context.GameContext;

import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.mainmenu.MainMenuData;
import context.mainmenu.MainMenuInput;
import context.mainmenu.MainMenuLogic;
import context.mainmenu.MainMenuVisuals;
import context.visuals.GameVisuals;

public final class LoadingGameLogic extends GameLogic {

	private LoadingGameVisuals visuals;

	@Override
	protected void init() {
		visuals = (LoadingGameVisuals) context().visuals();
	}

	@Override
	public void update() {
		if (visuals.done) {
			transitionToMainMenu();
		}
	}

	private void transitionToMainMenu() {
		GameData data = new MainMenuData();
		GameInput input = new MainMenuInput();
		GameLogic logic = new MainMenuLogic();
		GameVisuals visuals = new MainMenuVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		context().transition(context);
	}

}
