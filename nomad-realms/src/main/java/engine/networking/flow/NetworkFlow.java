package engine.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

public abstract class NetworkFlow {

	protected abstract boolean test(SyncedEvent event, PacketAddress address);

	protected abstract NetworkFlow handle(SyncedEvent event, PacketAddress address);

}
