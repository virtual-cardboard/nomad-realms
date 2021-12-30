package event.game.playerinput;

public class PeerConnectEvent extends NomadRealmsPlayerInputEvent {

	private long nonce;
	private long timestamp;

	public PeerConnectEvent(long timestamp, long nonce) {
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
