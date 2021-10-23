package context.connect;

import context.GameContext;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.logic.GameLogic;

public class PeerConnectLogic extends GameLogic {

	@Override
	public void update() {
		PeerConnectData data = (PeerConnectData) context().data();
		if (data.isConnected()) {
			transitionToGame();
		}
	}

	private void transitionToGame() {
		NomadsGameData data = new NomadsGameData();
		NomadsGameInput input = new NomadsGameInput();
		NomadsGameLogic logic = new NomadsGameLogic();
		NomadsGameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		context().transition(context);
	}

}
