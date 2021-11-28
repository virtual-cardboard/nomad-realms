package context.bootstrap;

import context.GameContext;
import context.bootstrap.logic.BootstrapResponseEventHandler;
import context.connect.PeerConnectData;
import context.connect.PeerConnectInput;
import context.connect.PeerConnectLogic;
import context.connect.PeerConnectVisuals;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import event.network.bootstrap.BootstrapResponseEvent;

public final class BootstrapGameLogic extends GameLogic {

	private BootstrapGameData data;

	@Override
	protected void init() {
		data = (BootstrapGameData) context().data();
		addHandler(BootstrapResponseEvent.class, new BootstrapResponseEventHandler(data));
	}

	@Override
	public void update() {
		if (data.matched()) {
			transitionToPeerConnect();
		}
	}

	protected GameContext transitionToPeerConnect() {
		GameData data = new PeerConnectData();
		GameInput input = new PeerConnectInput();
		GameLogic logic = new PeerConnectLogic(this.data.response());
		GameVisuals visuals = new PeerConnectVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		return context;
	}

}
