package event.network.p2p.time;

import static java.lang.System.currentTimeMillis;
import static networking.protocols.NomadRealmsP2PNetworkProtocol.TIME_REQUEST_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public class TimeRequestEvent extends NomadRealmsP2PNetworkEvent {

	private transient long time;

	public TimeRequestEvent() {
		time = currentTimeMillis();
	}

	public TimeRequestEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsP2PNetworkProtocol formatEnum() {
		return TIME_REQUEST_EVENT;
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
