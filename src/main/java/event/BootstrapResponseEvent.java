package event;

import common.event.GameEvent;
import common.source.GameSource;
import context.input.networking.packet.address.PacketAddress;

public class BootstrapResponseEvent extends GameEvent {

	private long nonce;
	private long timestamp;
	private PacketAddress lanAddress;
	private PacketAddress wanAddress;

	public BootstrapResponseEvent(GameSource source, long timestamp, long nonce, PacketAddress lanAddress, PacketAddress wanAddress) {
		super(source);
		this.timestamp = timestamp;
		this.nonce = nonce;
		this.lanAddress = lanAddress;
		this.wanAddress = wanAddress;
	}

	public PacketAddress getLanAddress() {
		return lanAddress;
	}

	public PacketAddress getWanAddress() {
		return wanAddress;
	}

	public long getNonce() {
		return nonce;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
