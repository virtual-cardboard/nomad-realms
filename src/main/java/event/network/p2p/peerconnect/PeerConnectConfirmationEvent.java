package event.network.p2p.peerconnect;

import static networking.protocols.NomadRealmsP2PNetworkProtocol.PEER_CONNECT_CONFIRMATION_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public class PeerConnectConfirmationEvent extends NomadRealmsP2PNetworkEvent {

	private long spawnPos;

	public PeerConnectConfirmationEvent() {
	}

	public PeerConnectConfirmationEvent(long spawnPos) {
		this.spawnPos = spawnPos;
	}

	public PeerConnectConfirmationEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsP2PNetworkProtocol formatEnum() {
		return PEER_CONNECT_CONFIRMATION_EVENT;
	}

	@Override
	public void read(SerializationReader reader) {
		this.spawnPos = reader.readLong();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(spawnPos);
	}

	public long spawnPos() {
		return spawnPos;
	}

}
