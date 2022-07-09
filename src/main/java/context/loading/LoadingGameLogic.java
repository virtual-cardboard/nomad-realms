package context.loading;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.audio.GameAudio;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.mainmenu.MainMenuData;
import context.mainmenu.MainMenuInput;
import context.mainmenu.MainMenuLogic;
import context.mainmenu.MainMenuVisuals;
import context.visuals.GameVisuals;

public final class LoadingGameLogic extends GameLogic {

	private LoadingGameData data;
	private LoadingGameVisuals visuals;
	private LoadingGameAudio audio;

	@Override
	protected void init() {
		visuals = (LoadingGameVisuals) context().visuals();
		audio = (LoadingGameAudio) context().audio();
		data = (LoadingGameData) context().data();
	}

	@Override
	public void update() {
		if (visuals.done && audio.done) {
			transitionToMainMenu();
		}
	}

	private void transitionToMainMenu() {
		GameAudio audio = new DefaultGameAudio();
		GameData data = new MainMenuData(this.data.tools());
		GameInput input = new MainMenuInput();
		GameLogic logic = new MainMenuLogic();
		GameVisuals visuals = new MainMenuVisuals();
		GameContext context = new GameContext(audio, data, input, logic, visuals);
		context().transition(context);
	}

}
