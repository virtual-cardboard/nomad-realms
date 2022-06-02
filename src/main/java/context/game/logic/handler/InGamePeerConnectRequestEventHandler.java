package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import engine.common.networking.packet.PacketModel;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.peerconnect.PeerConnectResponseEvent;

public class InGamePeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private final NomadsGameData data;
	private String username;
	private Queue<PacketModel> networkSend;

	public InGamePeerConnectRequestEventHandler(NomadsGameData data, String username, Queue<PacketModel> networkSend) {
		this.data = data;
		this.username = username;
		this.networkSend = networkSend;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent(data.joiningPlayerNonce(), username);
		networkSend.add(event.toPacketModel(t.source().address()));
	}

}
