package context.game.logic.handler;

import static java.lang.System.currentTimeMillis;

import java.util.Queue;
import java.util.function.Consumer;

import engine.common.networking.packet.PacketModel;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.peerconnect.PeerConnectResponseEvent;

public class InGamePeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private long nonce;
	private String username;
	private Queue<PacketModel> networkSend;

	public InGamePeerConnectRequestEventHandler(long nonce, String username, Queue<PacketModel> networkSend) {
		this.nonce = nonce;
		this.username = username;
		this.networkSend = networkSend;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent(currentTimeMillis(), nonce, username);
		networkSend.add(event.toPacketModel(t.source().address()));
	}

}
