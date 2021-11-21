package context.game.logic;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.connect.PeerConnectResponseEvent;

public class PeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private Queue<GameEvent> networkSync;
	private Queue<GameEvent> visualSync;

	public PeerConnectRequestEventHandler(Queue<GameEvent> networkSync, Queue<GameEvent> visualSync) {
		this.networkSync = networkSync;
		this.visualSync = visualSync;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent();
		networkSync.add(event);
		visualSync.add(event);
	}

}
