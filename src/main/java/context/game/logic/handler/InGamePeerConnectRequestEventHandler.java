package context.game.logic.handler;

import static java.lang.System.currentTimeMillis;

import java.util.Queue;
import java.util.function.Consumer;

import event.network.NomadRealmsP2PNetworkEvent;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.peerconnect.PeerConnectResponseEvent;

public class InGamePeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private long nonce;
	private String username;
	private Queue<NomadRealmsP2PNetworkEvent> outgoingNetworkEvents;

	public InGamePeerConnectRequestEventHandler(long nonce, String username, Queue<NomadRealmsP2PNetworkEvent> outgoingNetworkEvents) {
		this.nonce = nonce;
		this.username = username;
		this.outgoingNetworkEvents = outgoingNetworkEvents;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent(currentTimeMillis(), nonce, username);
		outgoingNetworkEvents.add(event);
	}

}
