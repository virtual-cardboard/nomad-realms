package event.network.c2s;

import static networking.protocols.NomadRealmsC2SNetworkProtocol.JOIN_CLUSTER_SUCCESS_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsC2SNetworkEvent;
import networking.protocols.NomadRealmsC2SNetworkProtocol;

public class JoinClusterSuccessEvent extends NomadRealmsC2SNetworkEvent {

	public JoinClusterSuccessEvent() {
	}

	public JoinClusterSuccessEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsC2SNetworkProtocol formatEnum() {
		return JOIN_CLUSTER_SUCCESS_EVENT;
	}

	@Override
	public void read(SerializationReader reader) {
	}

	@Override
	public void write(SerializationWriter writer) {
	}

}
