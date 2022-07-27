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
	private final long playerId = 1; // Change me
	private JoinClusterResponseEvent responseEvent;
	private JoinClusterData data;

	@Override
	protected void logic() {
		data = (JoinClusterData) context().data();
		if (SKIP_NETWORKING) {
			return;
		}
		GameTime gameTime = data.gameTime();
		JoinClusterRequestEvent joinClusterRequestEvent = new JoinClusterRequestEvent(LOCAL_HOST.address(), 0, playerId);
		data.tools().logMessage("Executing JoinClusterRequestEvent...");
		String urlPath = SERVER_HTTP_URL + "/join";
		long time = gameTime.currentTimeMillis();
		responseEvent = new JoinClusterResponseEvent(joinClusterRequestEvent.toHttpRequestModel(SERVER_HTTP_URL + "/join").execute());
		data.tools().logMessage("Received JoinClusterResponseEvent in " + (gameTime.currentTimeMillis() - time) + "ms.");
		data.tools().logMessage("|     Peer lan addresses: " + responseEvent.lanAddresses());
		data.tools().logMessage("|     Peer wan addresses: " + responseEvent.wanAddresses());
		data.tools().logMessage("|     Nonce: " + responseEvent.nonce());
		data.tools().logMessage("|     Scheduled to spawn in " + (responseEvent.spawnTime() - gameTime.currentTimeMillis()) + "ms.");
		data.tools().logMessage("|     Absolute spawn time: " + responseEvent.spawnTime());
		data.tools().logMessage("|     Spawn tick: " + responseEvent.spawnTick());
		data.tools().logMessage("");
	}

	@Override
	protected GameContext nextContext() {
		data.tools().logMessage("Transitioning to Peer Connect", 0x29cf3aff);
		GameData data = new PeerConnectData(this.data.gameTime(), this.data.tools(), responseEvent, playerId);
		GameInput input = new PeerConnectInput();
		GameLogic logic = new PeerConnectLogic();
		GameVisuals visuals = new PeerConnectVisuals();
		return new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
	}

}
