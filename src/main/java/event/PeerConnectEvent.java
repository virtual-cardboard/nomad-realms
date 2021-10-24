package event;

import common.event.GameEvent;
import common.source.GameSource;

public class PeerConnectEvent extends GameEvent {

	private static final long serialVersionUID = -2268329105716215451L;

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
