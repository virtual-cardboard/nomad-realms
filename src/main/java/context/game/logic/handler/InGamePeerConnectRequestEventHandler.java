package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import event.network.NomadRealmsNetworkEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import event.network.peerconnect.PeerConnectResponseEvent;

public class InGamePeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private long nonce;
	private String username;
	private Queue<NomadRealmsNetworkEvent> outgoingNetworkEvents;

	public InGamePeerConnectRequestEventHandler(long nonce, String username, Queue<NomadRealmsNetworkEvent> outgoingNetworkEvents) {
		this.nonce = nonce;
		this.username = username;
		this.outgoingNetworkEvents = outgoingNetworkEvents;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent(nonce, username);
		outgoingNetworkEvents.add(event);
	}

}
