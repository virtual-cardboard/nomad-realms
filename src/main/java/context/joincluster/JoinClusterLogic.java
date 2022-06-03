package context.joincluster;

import static app.NomadRealmsClient.SKIP_NETWORKING;
import static java.lang.System.currentTimeMillis;
import static networking.ClientNetworkUtils.LOCAL_HOST;
import static networking.ClientNetworkUtils.SERVER;
import static networking.ClientNetworkUtils.SERVER_HTTP_URL;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.logic.TimeInsensitiveGameLogic;
import context.peerconnect.PeerConnectData;
import context.peerconnect.PeerConnectInput;
import context.peerconnect.PeerConnectLogic;
import context.peerconnect.PeerConnectVisuals;
import context.visuals.GameVisuals;
import engine.common.networking.packet.HttpRequestModel;
import engine.common.networking.packet.PacketModel;
import event.network.c2s.JoinClusterRequestEvent;
import event.network.c2s.JoinClusterResponseEvent;
import event.network.c2s.TimeRequestEvent;

public final class JoinClusterLogic extends TimeInsensitiveGameLogic {

	private static final String SERVER_URL = "http://99.250.93.242:45001";
	private final String username = "JaryJay";
	private JoinClusterResponseEvent responseEvent;

	@Override
	protected void logic() {
		if (SKIP_NETWORKING) {
			return;
		}
		context().sendPacket(new PacketModel(new byte[0], SERVER.address()));
		JoinClusterRequestEvent joinClusterRequestEvent = new JoinClusterRequestEvent(LOCAL_HOST.address(), 0, username);
		System.out.println("Executing JoinClusterResponseEvent...");
		String urlPath = SERVER_HTTP_URL + "/join";
		HttpRequestModel request0 = new TimeRequestEvent().toHttpRequestModel(SERVER_HTTP_URL + "/time");
		responseEvent = new JoinClusterResponseEvent(joinClusterRequestEvent.toHttpRequestModel(SERVER_HTTP_URL + "/join").execute());
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
