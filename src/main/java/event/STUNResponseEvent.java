package event;

import common.event.GameEvent;
import common.source.GameSource;
import context.input.networking.packet.address.PacketAddress;

public class STUNResponseEvent extends GameEvent {

	private PacketAddress address;
	private long nonce;
	private long timestamp;

	public STUNResponseEvent(long time, GameSource source, long timestamp, long nonce, PacketAddress address) {
		super(time, source);
		this.timestamp = timestamp;
		this.nonce = nonce;
		this.address = address;
	}

	public PacketAddress getAddress() {
		return address;
	}

	public long getNonce() {
		return nonce;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
