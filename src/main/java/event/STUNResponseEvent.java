package event;

import common.event.GameEvent;
import common.source.GameSource;
import context.input.networking.packet.address.PacketAddress;

public class STUNResponseEvent extends GameEvent {

	private PacketAddress address;

	public STUNResponseEvent(long time, GameSource source, PacketAddress address) {
		super(time, source);
		this.address = address;
	}

	public PacketAddress getAddress() {
		return address;
	}

}
