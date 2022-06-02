package context.connect;

import static app.NomadRealmsClient.SKIP_NETWORKING;
import static context.connect.PeerConnectData.MAX_RETRIES;
import static context.connect.PeerConnectData.TIMEOUT_MILLISECONDS;
import static java.lang.System.currentTimeMillis;

import java.util.List;

import context.GameContext;
import context.audio.GameAudio;
import context.connect.logic.PeerConnectRequestEventHandler;
import context.connect.logic.PeerConnectResponseEventHandler;
import context.data.GameData;
import context.game.NomadsGameAudio;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.input.GameInput;
import context.logic.GameLogic;
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
				successEvent.toHttpRequestModel("http://99.250.93.242:45001/joinSuccess").execute();
			}
			transitionToGame();
			return;
		}
		long time = currentTimeMillis();
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
				data.setLastTriedTime(time);
				data.incrementTimesTried();
				System.out.println("Trying to connect to peer: LAN=" + lan + " WAN=" + wan);
				System.out.println(context().socketPort() & 0xFFFF);
			}
		}
	}

	private void transitionToGame() {
		System.out.println("Transitioning to the game!!!!");
		GameAudio audio = new NomadsGameAudio();
		GameData data = new NomadsGameData();
		GameInput input = new NomadsGameInput();
		GameLogic logic = new NomadsGameLogic(this.data.username());
		GameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(audio, data, input, logic, visuals);
		context().transition(context);
	}

}
