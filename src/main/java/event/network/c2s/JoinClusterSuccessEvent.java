package event.network.c2s;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsC2SNetworkEvent;

public class JoinClusterSuccessEvent extends NomadRealmsC2SNetworkEvent {

	public JoinClusterSuccessEvent() {
	}

	public JoinClusterSuccessEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public void read(SerializationReader reader) {
	}

	@Override
	public void write(SerializationWriter writer) {
	}

}
