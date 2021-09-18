package context.loading;

import context.GameContext;
import context.connect.PeerConnectData;
import context.connect.PeerConnectLogic;
import context.connect.PeerConnectVisuals;
import context.input.DefaultGameInput;
import context.logic.GameLogic;

public class LoadingLogic extends GameLogic {

	@Override
	public void update() {
		LoadingData loadingData = (LoadingData) getContext().getData();
		if (loadingData.isDone()) {
			transitionToPeerConnect();
		}
	}

	private void transitionToPeerConnect() {
		PeerConnectData data = new PeerConnectData();
		DefaultGameInput input = new DefaultGameInput();
		PeerConnectVisuals visuals = new PeerConnectVisuals();
		PeerConnectLogic logic = new PeerConnectLogic();
		GameContext context = new GameContext(data, input, logic, visuals);
		getContext().getWrapper().transition(context);
	}

}
