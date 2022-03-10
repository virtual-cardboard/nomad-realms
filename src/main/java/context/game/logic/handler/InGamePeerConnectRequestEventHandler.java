package context.game.logic.handler;

import java.util.function.Consumer;

import event.network.peerconnect.PeerConnectRequestEvent;
import event.network.peerconnect.PeerConnectResponseEvent;

public class InGamePeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private long nonce;
	private String username;

	public InGamePeerConnectRequestEventHandler(long nonce, String username) {
		this.nonce = nonce;
		this.username = username;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent(nonce, username);
	}

}
