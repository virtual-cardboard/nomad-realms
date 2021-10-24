package context.loading;

import context.GameContext;
import context.connect.PeerConnectData;
import context.connect.PeerConnectInput;
import context.connect.PeerConnectLogic;
import context.connect.PeerConnectVisuals;
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
		GameData data = new PeerConnectData();
		GameInput input = new PeerConnectInput();
		GameLogic logic = new PeerConnectLogic();
		GameVisuals visuals = new PeerConnectVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		context().transition(context);
	}

}
