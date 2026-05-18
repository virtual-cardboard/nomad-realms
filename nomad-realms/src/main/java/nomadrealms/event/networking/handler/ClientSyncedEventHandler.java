package nomadrealms.event.networking.handler;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
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
import nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.bootstrap.OnlinePlayersListEvent;
import nomadrealms.event.networking.HolePunchInitiationEvent;
import nomadrealms.event.networking.HolePunchInitiationInfoPackageEvent;
import nomadrealms.event.networking.HolePunchInitiationIntentEvent;
import nomadrealms.event.networking.HolePunchEvent;
import nomadrealms.event.networking.HolePunchSuccessConfirmationEvent;
import nomadrealms.event.networking.HolePunchSuccessAcknowledgementEvent;
import nomadrealms.networking.Connection;
import nomadrealms.networking.ConnectionState;
import nomadrealms.networking.NetworkGraph;
import nomadrealms.user.Player;

public class ClientSyncedEventHandler implements SyncedEventHandler {

	private List<Player> onlinePlayers;
	private NetworkGraph networkGraph;

	public ClientSyncedEventHandler() {
	}

	public ClientSyncedEventHandler(List<Player> onlinePlayers, NetworkGraph networkGraph) {
		this.onlinePlayers = onlinePlayers;
		this.networkGraph = networkGraph;
	}

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
	public void resolve(DisconnectFromServerEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(GetOnlinePlayersEvent event, PacketAddress address) {
	}

	@Override
	public void resolve(OnlinePlayersListEvent event, PacketAddress address) {
		System.out.println("Received OnlinePlayersListEvent from server " + address);
		if (onlinePlayers != null) {
			onlinePlayers.clear();
			onlinePlayers.addAll(event.players());
		}
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
	}

	@Override
	public void resolve(HolePunchInitiationInfoPackageEvent event, PacketAddress address) {
		for (Map.Entry<UUID, PacketAddress> entry : event.nonceToAddress().entrySet()) {
			UUID nonce = entry.getKey();
			PacketAddress peerAddress = entry.getValue();
			Player peer = onlinePlayers.stream()
					.filter(p -> p.address().equals(peerAddress))
					.findFirst()
					.orElse(null);
			if (peer != null) {
				networkGraph.addConnection(new Connection(peer, nonce));
			}
		}
	}

	@Override
	public void resolve(HolePunchInitiationIntentEvent event, PacketAddress address) {
		Player initiatorFromEvent = event.initiator();
		Player initiator = onlinePlayers.stream()
				.filter(p -> p.name().equals(initiatorFromEvent.name()))
				.findFirst()
				.orElseGet(() -> {
					onlinePlayers.add(initiatorFromEvent);
					return initiatorFromEvent;
				});
		networkGraph.addConnection(new Connection(initiator, event.nonce()));
	}

	@Override
	public void resolve(HolePunchEvent event, PacketAddress address) {
		networkGraph.getConnection(event.nonce()).ifPresent(connection -> {
			if (connection.state() == ConnectionState.LISTENING) {
				connection.state(ConnectionState.RECEIVING);
				connection.targetAddress(address);
				connection.player().address(address);
			}
		});
	}

	@Override
	public void resolve(HolePunchSuccessConfirmationEvent event, PacketAddress address) {
		networkGraph.getConnection(event.nonce()).ifPresent(connection -> {
			connection.state(ConnectionState.HEALTHY);
			connection.ackFramesLeft(60);
			connection.targetAddress(address);
			connection.player().address(address);
		});
	}

	@Override
	public void resolve(HolePunchSuccessAcknowledgementEvent event, PacketAddress address) {
		networkGraph.getConnection(event.nonce()).ifPresent(connection -> {
			connection.state(ConnectionState.HEALTHY);
			connection.ackFramesLeft(0);
			connection.targetAddress(address);
			connection.player().address(address);
		});
	}

}
