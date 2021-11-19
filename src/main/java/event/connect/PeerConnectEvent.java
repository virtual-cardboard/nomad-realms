package event.connect;

import common.event.GameEvent;
import common.source.GameSource;

public class PeerConnectEvent extends GameEvent {

	private long nonce;
	private long timestamp;

	public PeerConnectEvent(GameSource source, long timestamp, long nonce) {
		super(source);
		this.timestamp = timestamp;
		this.nonce = nonce;
	}

	public long getNonce() {
		return nonce;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
