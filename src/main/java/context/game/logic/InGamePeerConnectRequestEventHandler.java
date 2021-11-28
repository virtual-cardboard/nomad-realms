package context.game.logic;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import event.network.peerconnect.PeerConnectResponseEvent;

public class InGamePeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private long nonce;
	private String username;
	private Queue<GameEvent> networkSync;
	private Queue<GameEvent> visualSync;

	public InGamePeerConnectRequestEventHandler(Queue<GameEvent> networkSync, Queue<GameEvent> visualSync, long nonce, String username) {
		this.networkSync = networkSync;
		this.visualSync = visualSync;
		this.nonce = nonce;
		this.username = username;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent(nonce, username);
		networkSync.add(event);
		visualSync.add(event);
	}

}
