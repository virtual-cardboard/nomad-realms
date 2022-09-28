package event.network.p2p.peerconnect;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import math.WorldPos;

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
