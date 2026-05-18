package nomadrealms.networking.flow;

import static nomadrealms.networking.flow.NetworkFlowBuilder.expect;
import static nomadrealms.networking.flow.NetworkRole.CLIENT;
import static nomadrealms.networking.flow.NetworkRole.SERVER;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.bootstrap.OnlinePlayersListEvent;
import nomadrealms.networking.Connection;
import nomadrealms.networking.ConnectionState;
import nomadrealms.user.Player;

public enum NetworkFlowTemplate {

	PING_PONG(
			expect(PingSyncedEvent.class)
					.thenSend(event -> new PongSyncedEvent("Pong from " + ((((PingSyncedEvent) event).message().contains("server")) ? "client" : "server"), System.currentTimeMillis()))
					.build()
	),

	CONNECT(
			onRole(SERVER, expect(ConnectToServerEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									ConnectToServerEvent connectEvent = (ConnectToServerEvent) event;
									Player newPlayer = new Player(connectEvent.name(), address);
									if (!context.onlinePlayers().stream().anyMatch(p -> p.name().equals(newPlayer.name()))) {
										context.onlinePlayers().add(newPlayer);
									}
									for (Player player : context.onlinePlayers()) {
										if (player.address() == null) continue;
										List<Player> otherPlayers = context.onlinePlayers().stream()
												.filter(p -> p.address() != null && !p.address().equals(player.address()))
												.collect(Collectors.toList());
										context.networkNode().send(new OnlinePlayersListEvent(otherPlayers), player.address());
									}
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	DISCONNECT(
			onRole(SERVER, expect(DisconnectFromServerEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									context.onlinePlayers().removeIf(player -> player.address() != null && player.address().equals(address));
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	GET_ONLINE_PLAYERS(
			onRole(SERVER, expect(GetOnlinePlayersEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									List<Player> otherPlayers = context.onlinePlayers().stream()
											.filter(player -> player.address() != null && !player.address().equals(address))
											.collect(Collectors.toList());
									context.networkNode().send(new OnlinePlayersListEvent(otherPlayers), address);
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	UPDATE_ONLINE_PLAYERS(
			onRole(CLIENT, expect(OnlinePlayersListEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									OnlinePlayersListEvent listEvent = (OnlinePlayersListEvent) event;
									context.onlinePlayers().clear();
									context.onlinePlayers().addAll(listEvent.players());
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	HOLE_PUNCH_INITIATION(
			onRole(SERVER, expect(HolePunchInitiationEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									Player initiator = context.onlinePlayers().stream()
											.filter(player -> player.address() != null && player.address().equals(address))
											.findFirst()
											.orElse(null);
									if (initiator == null) return;
									Map<UUID, engine.context.input.networking.packet.address.PacketAddress> nonceToAddress = new java.util.HashMap<>();
									for (Player otherPlayer : context.onlinePlayers()) {
										if (otherPlayer.address() == null || otherPlayer.address().equals(address)) continue;
										UUID nonce = UUID.randomUUID();
										nonceToAddress.put(nonce, otherPlayer.address());
										context.networkNode().send(new HolePunchInitiationIntentEvent(nonce, initiator), otherPlayer.address());
									}
									context.networkNode().send(new HolePunchInitiationInfoPackageEvent(nonceToAddress), address);
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	HOLE_PUNCH_INFO_PACKAGE(
			onRole(CLIENT, expect(HolePunchInitiationInfoPackageEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									HolePunchInitiationInfoPackageEvent infoEvent = (HolePunchInitiationInfoPackageEvent) event;
									for (Map.Entry<UUID, engine.context.input.networking.packet.address.PacketAddress> entry : infoEvent.nonceToAddress().entrySet()) {
										UUID nonce = entry.getKey();
										engine.context.input.networking.packet.address.PacketAddress peerAddress = entry.getValue();
										Player peer = context.onlinePlayers().stream()
												.filter(p -> p.address() != null && p.address().equals(peerAddress))
												.findFirst()
												.orElse(null);
										if (peer != null) {
											context.networkGraph().addConnection(new Connection(peer, nonce));
										}
									}
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	HOLE_PUNCH_INTENT(
			onRole(CLIENT, expect(HolePunchInitiationIntentEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									HolePunchInitiationIntentEvent intentEvent = (HolePunchInitiationIntentEvent) event;
									Player initiatorFromEvent = intentEvent.initiator();
									Player initiator = context.onlinePlayers().stream()
											.filter(p -> p.name().equals(initiatorFromEvent.name()))
											.findFirst()
											.orElseGet(() -> {
												context.onlinePlayers().add(initiatorFromEvent);
												return initiatorFromEvent;
											});
									context.networkGraph().addConnection(new Connection(initiator, intentEvent.nonce()));
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	HOLE_PUNCH_PING(
			expect(HolePunchEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									HolePunchEvent hpEvent = (HolePunchEvent) event;
									context.networkGraph().getConnection(hpEvent.nonce()).ifPresent(connection -> {
										if (connection.state() == ConnectionState.LISTENING) {
											connection.state(ConnectionState.RECEIVING);
											connection.targetAddress(address);
											connection.player().address(address);
										}
									});
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build()
	),

	HOLE_PUNCH_CONFIRMATION(
			onRole(CLIENT, expect(HolePunchSuccessConfirmationEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									HolePunchSuccessConfirmationEvent confEvent = (HolePunchSuccessConfirmationEvent) event;
									context.networkGraph().getConnection(confEvent.nonce()).ifPresent(connection -> {
										connection.state(ConnectionState.HEALTHY);
										connection.targetAddress(address);
										connection.player().address(address);
										context.networkNode().send(new HolePunchSuccessAcknowledgementEvent(confEvent.nonce()), address);
									});
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	HOLE_PUNCH_ACKNOWLEDGEMENT(
			onRole(CLIENT, expect(HolePunchSuccessAcknowledgementEvent.class)
					.then(new NetworkFlowTemplateNode() {
						@Override
						public boolean canTrigger(SyncedEvent event, NetworkRole role) {
							return true;
						}

						@Override
						public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
							return new NetworkFlow() {
								@Override
								public boolean consume(SyncedEvent e, NetworkFlowContext context) {
									return false;
								}

								@Override
								public void update(NetworkFlowContext context) {
									HolePunchSuccessAcknowledgementEvent ackEvent = (HolePunchSuccessAcknowledgementEvent) event;
									context.networkGraph().getConnection(ackEvent.nonce()).ifPresent(connection -> {
										connection.state(ConnectionState.HEALTHY);
										connection.targetAddress(address);
										connection.player().address(address);
									});
								}

								@Override
								public boolean isCompleted() {
									return true;
								}
							};
						}
					})
					.build())
	),

	PASS_THROUGH_INPUT(
			expect(InputEvent.class).build()
	),

	PASS_THROUGH_CARD(
			expect(CardPlayedEvent.class).build()
	),

	PASS_THROUGH_DROP(
			expect(DropItemEvent.class).build()
	),

	PASS_THROUGH_INTERACT(
			expect(InteractEvent.class).build()
	);

	private final NetworkFlowTemplateNode root;

	NetworkFlowTemplate(NetworkFlowTemplateNode root) {
		this.root = root;
	}

	public static NetworkFlowTemplateNode onRole(NetworkRole role, NetworkFlowTemplateNode node) {
		return new OnRoleNode(role, node);
	}

	public Optional<NetworkFlow> tryTrigger(SyncedEvent event, NetworkRole role, PacketAddress address) {
		if (root.canTrigger(event, role)) {
			return Optional.of(root.createFlow(event, role, address));
		}
		return Optional.empty();
	}

}
