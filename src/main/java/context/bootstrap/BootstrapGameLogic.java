package context.bootstrap;

import static networking.ClientNetworkUtils.LOCAL_HOST;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.connect.PeerConnectData;
import context.connect.PeerConnectInput;
import context.connect.PeerConnectLogic;
import context.connect.PeerConnectVisuals;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.logic.TimeInsensitiveGameLogic;
import context.visuals.GameVisuals;
import event.network.c2s.JoinClusterRequestEvent;
import event.network.c2s.JoinClusterResponseEvent;

public final class BootstrapGameLogic extends TimeInsensitiveGameLogic {

	private final String username = "JaryJay";
	private JoinClusterResponseEvent responseEvent;

	@Override
	protected void logic() {
		JoinClusterRequestEvent joinClusterRequestEvent = new JoinClusterRequestEvent(LOCAL_HOST.address(), 0, username);
		responseEvent = new JoinClusterResponseEvent(joinClusterRequestEvent.toHttpRequestModel("http://99.250.93.242:45001/join").execute());
		System.out.println("Received join cluster response:");
		System.out.println(responseEvent.lanAddresses());
		System.out.println(responseEvent.wanAddresses());
		System.out.println(responseEvent.nonce());
	}

	@Override
	protected GameContext nextContext() {
		GameData data = new PeerConnectData(responseEvent, username);
		GameInput input = new PeerConnectInput();
		GameLogic logic = new PeerConnectLogic();
		GameVisuals visuals = new PeerConnectVisuals();
		return new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
	}

}
