package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;

@Derializable
public class HolePunchInitiationEvent implements SyncedEvent {

	public HolePunchInitiationEvent() {
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
