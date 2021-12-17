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

public class MainMenuLogic extends GameLogic {

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	private void transitionToConnect() {
		GameData data = new BootstrapGameData();
		GameInput input = new BootstrapGameInput();
		GameLogic logic = new BootstrapGameLogic();
		GameVisuals visuals = new BootstrapGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		context().transition(context);
	}

}
