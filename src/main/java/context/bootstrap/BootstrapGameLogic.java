package context.bootstrap;

import static java.lang.System.currentTimeMillis;
import static networking.ClientNetworkUtils.LOCAL_HOST;
import static networking.ClientNetworkUtils.SERVER;

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
import engine.common.networking.packet.PacketModel;
import event.network.c2s.JoinClusterRequestEvent;
import event.network.c2s.JoinClusterResponseEvent;

public final class BootstrapGameLogic extends TimeInsensitiveGameLogic {

	private static final String SERVER_URL = "http://99.250.93.242:45001";
	private final String username = "JaryJay";
	private JoinClusterResponseEvent responseEvent;

	@Override
	protected void logic() {
		context().sendPacket(new PacketModel(new byte[0], SERVER.address()));
		JoinClusterRequestEvent joinClusterRequestEvent = new JoinClusterRequestEvent(LOCAL_HOST.address(), 0, username);
		responseEvent = new JoinClusterResponseEvent(joinClusterRequestEvent.toHttpRequestModel(SERVER_URL + "/join").execute());
		System.out.println("Received join cluster response:");
		System.out.println(responseEvent.lanAddresses());
		System.out.println(responseEvent.wanAddresses());
		System.out.println(responseEvent.nonce());
		System.out.println("Scheduled to spawn in " + (responseEvent.spawnPlayerTime() - currentTimeMillis()) + "ms.");
	}

	@Override
	protected GameContext nextContext() {
		System.out.println("Transitioning to Peer Connect");
		GameData data = new PeerConnectData(responseEvent, username);
		GameInput input = new PeerConnectInput();
		GameLogic logic = new PeerConnectLogic();
		GameVisuals visuals = new PeerConnectVisuals();
		return new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
	}

}
