package context.game;

import context.GameContext;
import context.logic.GameLogic;

public class NomadsGameLogic extends GameLogic {

	@Override
	public void update() {
		NomadsGameData data = (NomadsGameData) getContext().getData();
		if (data.isConnected()) {
			GameContext context = new GameContext(data, null, null, null);
			getContext().getWrapper().transition(context);
		}
	}

}
