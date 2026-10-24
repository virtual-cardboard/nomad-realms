package nomadrealms.event.networking.handler;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InteractEvent;
import nomadrealms.event.networking.HolePunchEvent;
import nomadrealms.event.networking.HolePunchInitiationEvent;
import nomadrealms.event.networking.HolePunchInitiationInfoPackageEvent;
import nomadrealms.event.networking.HolePunchInitiationIntentEvent;
import nomadrealms.event.networking.HolePunchSuccessAcknowledgementEvent;
import nomadrealms.event.networking.HolePunchSuccessConfirmationEvent;
import nomadrealms.event.networking.PingSyncedEvent;
import nomadrealms.event.networking.PongSyncedEvent;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.event.networking.SyncedEventHandler;
import nomadrealms.event.networking.bootstrap.BootstrapEvent;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.bootstrap.OnlinePlayersListEvent;
import nomadrealms.render.ui.custom.console.Console;
import nomadrealms.user.Player;

public class ServerSyncedEventHandler implements SyncedEventHandler {

	private final NetworkNode networkNode;
	private final List<Player> onlinePlayers;
	private final Console console;

	public ServerSyncedEventHandler(NetworkNode networkNode, List<Player> onlinePlayers, Console console) {
		this.networkNode = networkNode;
		this.onlinePlayers = onlinePlayers;
		this.console = console;
	}

	/**
	 * Logs a message to the console and standard output. Does nothing but print to standard output if no console is
	 * present, e.g. when running headless.
	 *
	 * @param message the message to log
	 */
	private void log(String message) {
		System.out.println(message);
		if (console != null) {
			console.println(message);
		}
	}

	@Override
	public void resolve(SyncedEvent event, PacketAddress address) {
		log("Received unknown SyncedEvent from " + address + ": " + event);
	}

	@Override
	public void resolve(PingSyncedEvent event, PacketAddress address) {
		log("Received Ping message from " + address + ": " + event.message());
		log("Ping timestamp: " + event.timestamp());
		networkNode.send(new PongSyncedEvent("Pong from server", System.currentTimeMillis()), address);
	}

	@Override
	public void resolve(PongSyncedEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(BootstrapEvent event, PacketAddress address) {
		log("Unhandled BootstrapEvent " + event + " from " + address);
	}

	@Override
	public void resolve(ConnectToServerEvent event, PacketAddress address) {
		Player newPlayer = new Player(event.name(), address);
		log("Player connected: " + event.name() + " from " + address);
		onlinePlayers.add(newPlayer);
		for (Player player : onlinePlayers) {
			sendOnlinePlayersList(player.address());
		}
	}

	@Override
	public void resolve(DisconnectFromServerEvent event, PacketAddress address) {
		log("Player disconnected: " + event.name() + " from " + address);
		onlinePlayers.removeIf(player -> player.address().equals(address));
	}

	@Override
	public void resolve(GetOnlinePlayersEvent event, PacketAddress address) {
		sendOnlinePlayersList(address);
	}

	private void sendOnlinePlayersList(PacketAddress address) {
		List<Player> otherPlayers = onlinePlayers.stream()
				.filter(player -> !player.address().equals(address))
				.collect(Collectors.toList());

		log("Sending online players list to " + address + " (count: " + otherPlayers.size() + ")");
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

	@Override
	public void resolve(HolePunchInitiationEvent event, PacketAddress address) {
		Player initiator = onlinePlayers.stream()
				.filter(player -> player.address().equals(address))
				.findFirst()
				.orElse(null);
		if (initiator == null) {
			return;
		}

		Map<UUID, PacketAddress> nonceToAddress = new HashMap<>();
		for (Player otherPlayer : onlinePlayers) {
			if (otherPlayer.address().equals(address)) {
				continue;
			}
			UUID nonce = UUID.randomUUID();
			nonceToAddress.put(nonce, otherPlayer.address());
			networkNode.send(new HolePunchInitiationIntentEvent(nonce, initiator), otherPlayer.address());
		}
		networkNode.send(new HolePunchInitiationInfoPackageEvent(nonceToAddress), address);
	}

	@Override
	public void resolve(HolePunchInitiationInfoPackageEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(HolePunchInitiationIntentEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(HolePunchEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(HolePunchSuccessConfirmationEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(HolePunchSuccessAcknowledgementEvent event, PacketAddress address) {
	}

}
