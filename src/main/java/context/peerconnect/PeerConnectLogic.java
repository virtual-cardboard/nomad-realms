package context.peerconnect;

import static app.NomadRealmsClient.SKIP_NETWORKING;
import static context.peerconnect.PeerConnectData.MAX_RETRIES;
import static context.peerconnect.PeerConnectData.TIMEOUT_MILLISECONDS;
import static networking.ClientNetworkUtils.SERVER_HTTP_URL;

import java.util.List;

import context.GameContext;
import context.audio.GameAudio;
import context.data.GameData;
import context.game.NomadsGameAudio;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.input.GameInput;
import context.logic.GameLogic;
import context.peerconnect.logic.PeerConnectRequestEventHandler;
import context.peerconnect.logic.PeerConnectResponseEventHandler;
import context.visuals.GameVisuals;
import engine.common.event.GameEvent;
import engine.common.networking.packet.address.PacketAddress;
import event.network.c2s.JoinClusterSuccessEvent;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.peerconnect.PeerConnectResponseEvent;

public class PeerConnectLogic extends GameLogic {

	private PeerConnectData data;

	@Override
	protected void init() {
		data = (PeerConnectData) context().data();

		addHandler(PeerConnectRequestEvent.class, new PeerConnectRequestEventHandler(data, context().networkSend()));
		addHandler(PeerConnectResponseEvent.class, new PeerConnectResponseEventHandler(data, context().networkSend()));
		addHandler(GameEvent.class, event -> System.out.println("Received " + event.getClass().getSimpleName()));
	}

	@Override
	public void update() {
		if (data.isConnected()) {
			// Send a JoinClusterSuccessEvent to server
			if (!SKIP_NETWORKING) {
				JoinClusterSuccessEvent successEvent = new JoinClusterSuccessEvent();
				successEvent.toHttpRequestModel(SERVER_HTTP_URL + "/joinSuccess").execute();
			}
			transitionToGame();
			return;
		}
		long time = data.gameTime().currentTimeMillis();
		if (data.timesTried() >= MAX_RETRIES) {
			System.out.println("Failed to connect!");
		} else if (time - data.lastTriedTime() >= TIMEOUT_MILLISECONDS) {
			List<PacketAddress> lanAddresses = data.unconnectedLanAddresses;
			List<PacketAddress> wanAddresses = data.unconnectedWanAddresses;
			for (int i = 0; i < lanAddresses.size(); i++) {
				PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(data.nonce(), data.username());
				PacketAddress lan = lanAddresses.get(i);
				PacketAddress wan = wanAddresses.get(i);
				context().sendPacket(connectRequest.toPacketModel(lan));
				context().sendPacket(connectRequest.toPacketModel(wan));
				System.out.println(lan);
				System.out.println(wan);
				System.out.println("Trying to connect to peer: LAN=" + lan + " WAN=" + wan);
				System.out.println(context().socketPort() & 0xFFFF);
			}
			data.setLastTriedTime(time);
			data.incrementTimesTried();
		}
	}

	private void transitionToGame() {
		System.out.println("Transitioning to the game!!!!");
		GameAudio audio = new NomadsGameAudio();
		GameData nomadsGameData = new NomadsGameData(data.gameTime(), data.username());
		GameInput input = new NomadsGameInput();
		GameLogic logic = new NomadsGameLogic();
		GameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(audio, nomadsGameData, input, logic, visuals);
		context().transition(context);
	}

}
