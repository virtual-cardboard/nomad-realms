package nomadrealms.event.networking.handler;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InteractEvent;
import nomadrealms.event.networking.PingSyncedEvent;
import nomadrealms.event.networking.PongSyncedEvent;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.event.networking.SyncedEventHandler;
import nomadrealms.event.networking.bootstrap.BootstrapEvent;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.bootstrap.OnlinePlayersListEvent;

public class ClientSyncedEventHandler implements SyncedEventHandler {

	@Override
	public void resolve(SyncedEvent event, PacketAddress address) {
		System.out.println("Received unknown SyncedEvent from server " + address + ": " + event);
	}

	@Override
	public void resolve(PingSyncedEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(PongSyncedEvent event, PacketAddress address) {
		System.out.println("Received Pong from server " + address + ": " + event.message());
		System.out.println("Pong timestamp: " + event.timestamp());
	}

	@Override
	public void resolve(BootstrapEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(ConnectToServerEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(GetOnlinePlayersEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(OnlinePlayersListEvent event, PacketAddress address) {
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
