package event.game.playerinput;

import common.source.GameSource;

public class PeerConnectEvent extends NomadRealmsPlayerInputEvent {

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
