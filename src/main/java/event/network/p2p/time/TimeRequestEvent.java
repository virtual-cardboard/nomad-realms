package event.network.p2p.time;

import static java.lang.System.currentTimeMillis;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;

public class TimeRequestEvent extends NomadRealmsP2PNetworkEvent {

	private transient long time;

	public TimeRequestEvent() {
		time = currentTimeMillis();
	}

	public TimeRequestEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public void read(SerializationReader reader) {
	}

	@Override
	public void write(SerializationWriter writer) {
	}

	public long time() {
		return time;
	}

}
