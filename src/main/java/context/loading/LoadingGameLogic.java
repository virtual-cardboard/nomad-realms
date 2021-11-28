package context.loading;

import context.GameContext;
import context.bootstrap.BootstrapGameData;
import context.bootstrap.BootstrapGameInput;
import context.bootstrap.BootstrapGameLogic;
import context.bootstrap.BootstrapGameVisuals;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
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
			transitionToConnect();
		}
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
