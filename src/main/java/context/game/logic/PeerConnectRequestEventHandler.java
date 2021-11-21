package context.game.logic;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.connect.PeerConnectResponseEvent;

public class PeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private Queue<GameEvent> sync;

	public PeerConnectRequestEventHandler(Queue<GameEvent> sync) {
		this.sync = sync;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent();
		sync.add(event);
	}

}
