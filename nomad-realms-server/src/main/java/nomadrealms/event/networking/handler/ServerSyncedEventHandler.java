package nomadrealms.event.networking.handler;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.util.List;
import java.util.stream.Collectors;
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
import nomadrealms.user.Player;

public class ServerSyncedEventHandler implements SyncedEventHandler {

	private final NetworkNode networkNode;
	private final List<Player> onlinePlayers;

	public ServerSyncedEventHandler(NetworkNode networkNode, List<Player> onlinePlayers) {
		this.networkNode = networkNode;
		this.onlinePlayers = onlinePlayers;
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
	public void resolve(ConnectToServerEvent event, PacketAddress address) {
		Player newPlayer = new Player(event.name(), address);
		System.out.println("Player connected: " + event.name() + " from " + address);
		onlinePlayers.add(newPlayer);
	}

	@Override
	public void resolve(GetOnlinePlayersEvent event, PacketAddress address) {
		List<Player> otherPlayers = onlinePlayers.stream()
				.filter(player -> !player.address().equals(address))
				.collect(Collectors.toList());

		System.out.println("Sending online players list to " + address + " (count: " + otherPlayers.size() + ")");
		networkNode.send(new OnlinePlayersListEvent(otherPlayers), address);
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
