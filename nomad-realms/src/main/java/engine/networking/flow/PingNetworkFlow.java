package engine.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.PongSyncedEvent;
import nomadrealms.event.networking.SyncedEvent;

public class PingNetworkFlow extends NetworkFlow {

	private final PacketAddress serverAddress;

	public PingNetworkFlow(PacketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	@Override
	protected boolean test(SyncedEvent event, PacketAddress address) {
		return event instanceof PongSyncedEvent && address.equals(serverAddress);
	}

	@Override
	protected NetworkFlow handle(SyncedEvent event, PacketAddress address) {
		PongSyncedEvent e = (PongSyncedEvent) event;
		System.out.println("Received Pong from server " + address + ": " + e.message());
		System.out.println("Pong timestamp: " + e.timestamp());
		return null;
	}

}
