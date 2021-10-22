package context.loading;

import context.GameContext;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.logic.GameLogic;

public final class LoadingGameLogic extends GameLogic {

	@Override
	public void update() {
		LoadingGameVisuals visuals = (LoadingGameVisuals) context().visuals();
		if (visuals.done) {
			transitionToGame();
		}

	}

	private void transitionToGame() {
		context().transition(new GameContext(new NomadsGameData(), new NomadsGameInput(), new NomadsGameLogic(), new NomadsGameVisuals()));
	}

}
