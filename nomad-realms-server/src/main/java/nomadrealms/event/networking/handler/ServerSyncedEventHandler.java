package nomadrealms.event.networking.handler;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InteractEvent;
import nomadrealms.event.networking.PingSyncedEvent;
import nomadrealms.event.networking.PongSyncedEvent;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.event.networking.SyncedEventHandler;
import nomadrealms.event.networking.bootstrap.BootstrapEvent;

public class ServerSyncedEventHandler implements SyncedEventHandler {

	private final NetworkNode networkNode;

	public ServerSyncedEventHandler(NetworkNode networkNode) {
		this.networkNode = networkNode;
	}

	@Override
	public void resolve(SyncedEvent event, PacketAddress address) {
		System.out.println("Received unknown SyncedEvent from " + address + ": " + event);
	}

	@Override
	public void resolve(PingSyncedEvent event, PacketAddress address) {
		System.out.println("Received Ping message from " + address + ": " + event.message());
		System.out.println("Ping timestamp: " + event.timestamp());
		networkNode.send(new PongSyncedEvent("Pong from server", System.currentTimeMillis()), address);
	}

	@Override
	public void resolve(PongSyncedEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(BootstrapEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(InputEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(DropItemEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(CardPlayedEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(InteractEvent event, PacketAddress address) {
	}

}
