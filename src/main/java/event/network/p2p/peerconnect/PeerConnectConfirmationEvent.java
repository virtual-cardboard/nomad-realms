package event.network.p2p.peerconnect;

import static networking.protocols.NomadRealmsP2PNetworkProtocol.PEER_CONNECT_CONFIRMATION_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import math.WorldPos;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public class PeerConnectConfirmationEvent extends NomadRealmsP2PNetworkEvent {

	private WorldPos spawnPos;

	public PeerConnectConfirmationEvent() {
	}

	public PeerConnectConfirmationEvent(WorldPos spawnPos) {
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
		this.spawnPos = new WorldPos();
		this.spawnPos.read(reader);
	}

	@Override
	public void write(SerializationWriter writer) {
		spawnPos.write(writer);
	}

	public WorldPos spawnPos() {
		return spawnPos;
	}

}
