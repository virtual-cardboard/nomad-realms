package event.network.p2p.peerconnect;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;

public class PeerConnectResponseEvent extends NomadRealmsP2PNetworkEvent {

	private long nonce;
	private String username;

	public PeerConnectResponseEvent() {
	}

	public PeerConnectResponseEvent(long nonce, String username) {
		this.nonce = nonce;
		this.username = username;
	}

	public PeerConnectResponseEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public void read(SerializationReader reader) {
		this.nonce = reader.readLong();
		this.username = reader.readStringUtf8();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(nonce);
		writer.consume(username);
	}

	public long nonce() {
		return nonce;
	}

	public String username() {
		return username;
	}

}
