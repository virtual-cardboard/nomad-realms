package context.mainmenu;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.bootstrap.BootstrapGameInput;
import context.bootstrap.BootstrapGameLogic;
import context.bootstrap.BootstrapGameVisuals;
import context.data.DefaultGameData;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.visuals.GameVisuals;

public class MainMenuLogic extends GameLogic {

	@Override
	public void update() {
		GameData data = new DefaultGameData();
		GameInput input = new BootstrapGameInput();
		GameLogic logic = new BootstrapGameLogic();
		GameVisuals visuals = new BootstrapGameVisuals();
		GameContext context = new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
		context().transition(context);
	}

}
