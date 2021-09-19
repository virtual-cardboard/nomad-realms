package context.connect;

import context.GameContext;
import context.logic.GameLogic;

public class PeerConnectLogic extends GameLogic {

	@Override
	public void update() {
		PeerConnectData data = (PeerConnectData) context().data();
		if (data.isConnected()) {
			GameContext context = new GameContext(data, null, null, null);
			context().wrapper().transition(context);
		}
	}

}
