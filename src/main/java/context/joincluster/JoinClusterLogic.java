package context.joincluster;

import static app.NomadRealmsClient.SKIP_NETWORKING;
import static networking.ClientNetworkUtils.LOCAL_HOST;
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
import engine.common.time.GameTime;
import event.network.c2s.JoinClusterRequestEvent;
import event.network.c2s.JoinClusterResponseEvent;

public final class JoinClusterLogic extends TimeInsensitiveGameLogic {

	private static final String SERVER_URL = "http://99.250.93.242:45001";
	private final long playerId = 0; // Change me
	private JoinClusterResponseEvent responseEvent;
	private JoinClusterData joinClusterData;

	@Override
	protected void logic() {
		if (SKIP_NETWORKING) {
			return;
		}
		joinClusterData = (JoinClusterData) context().data();
		GameTime gameTime = joinClusterData.gameTime();
		JoinClusterRequestEvent joinClusterRequestEvent = new JoinClusterRequestEvent(LOCAL_HOST.address(), 0, playerId);
		System.out.println("Executing JoinClusterResponseEvent...");
		String urlPath = SERVER_HTTP_URL + "/join";
		long time = gameTime.currentTimeMillis();
		responseEvent = new JoinClusterResponseEvent(joinClusterRequestEvent.toHttpRequestModel(SERVER_HTTP_URL + "/join").execute());
		System.out.println("Received join cluster response in " + (gameTime.currentTimeMillis() - time) + "ms.");
		System.out.println("|   Peer lan addresses: " + responseEvent.lanAddresses());
		System.out.println("|   Peer wan addresses: " + responseEvent.wanAddresses());
		System.out.println("|   Nonce: " + responseEvent.nonce());
		System.out.println("|   Scheduled to spawn in " + (responseEvent.spawnTime() - gameTime.currentTimeMillis()) + "ms.");
		System.out.println("|   Absolute spawn time: " + responseEvent.spawnTime());
		System.out.println("|   Spawn tick: " + responseEvent.spawnTick());
	}

	@Override
	protected GameContext nextContext() {
		System.out.println("Transitioning to Peer Connect");
		GameData data = new PeerConnectData(joinClusterData.gameTime(), responseEvent, playerId);
		GameInput input = new PeerConnectInput();
		GameLogic logic = new PeerConnectLogic();
		GameVisuals visuals = new PeerConnectVisuals();
		return new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
	}

}