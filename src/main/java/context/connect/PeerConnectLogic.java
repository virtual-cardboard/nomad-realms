package context.connect;

import context.GameContext;
import context.logic.GameLogic;

public class PeerConnectLogic extends GameLogic {

	@Override
	public void update() {
		PeerConnectData data = (PeerConnectData) getContext().getData();
		if (data.isConnected()) {
			GameContext context = new GameContext(data, null, null, null);
			getContext().getWrapper().transition(context);
		}
	}

}
