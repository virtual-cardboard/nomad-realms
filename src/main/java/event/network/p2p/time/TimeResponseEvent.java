package event.network.p2p.time;

import static networking.protocols.NomadRealmsP2PNetworkProtocol.TIME_RESPONSE_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public class TimeResponseEvent extends NomadRealmsP2PNetworkEvent {

	private long receiveTime;
	private long sendTime;

	public TimeResponseEvent() {
	}

	public TimeResponseEvent(long receiveTime, long sendTime) {
		this.receiveTime = receiveTime;
		this.sendTime = sendTime;
	}

	public TimeResponseEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsP2PNetworkProtocol formatEnum() {
		return TIME_RESPONSE_EVENT;
	}

	@Override
	public void read(SerializationReader reader) {
		this.receiveTime = reader.readLong();
		this.sendTime = reader.readLong();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(receiveTime);
		writer.consume(sendTime);
	}

	public long receiveTime() {
		return receiveTime;
	}

	public long sendTime() {
		return sendTime;
	}

}
