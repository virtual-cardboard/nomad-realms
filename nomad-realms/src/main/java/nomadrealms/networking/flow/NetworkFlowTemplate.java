package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.Optional;
import java.util.function.BiPredicate;
import nomadrealms.event.networking.SyncedEvent;

import static nomadrealms.networking.flow.NetworkRole.CLIENT;
import static nomadrealms.networking.flow.NetworkRole.SERVER;

public enum NetworkFlowTemplate {

	PING_PONG(
			(event, role) -> role == SERVER && event instanceof nomadrealms.event.networking.PingSyncedEvent,
			new SequenceStep(
					new SendStep(
							flow -> new nomadrealms.event.networking.PongSyncedEvent("Pong from server", System.currentTimeMillis()),
							flow -> null // Use source address
					)
			)
	),

	BOOTSTRAP(
			(event, role) -> role == SERVER && (
					event instanceof nomadrealms.event.networking.bootstrap.ConnectToServerEvent ||
					event instanceof nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent ||
					event instanceof nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent
			),
			new SequenceStep(
					new BranchStep(
							ctx -> ctx.triggerEvent() instanceof nomadrealms.event.networking.bootstrap.ConnectToServerEvent,
							new SequenceStep(new AddPlayerStep(), new BroadcastOnlinePlayersStep()),
							new BranchStep(
									ctx -> ctx.triggerEvent() instanceof nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent,
									new RemovePlayerStep(),
									new GetOnlinePlayersStep()
							)
					)
			)
	),

	CLIENT_BOOTSTRAP(
			(event, role) -> role == CLIENT && event instanceof nomadrealms.event.networking.bootstrap.OnlinePlayersListEvent,
			new UpdateOnlinePlayersStep()
	),

	HOLE_PUNCH(
			(event, role) -> (role == SERVER && event instanceof nomadrealms.event.networking.HolePunchInitiationEvent) ||
					(role == CLIENT && (
							event instanceof nomadrealms.event.networking.HolePunchInitiationInfoPackageEvent ||
							event instanceof nomadrealms.event.networking.HolePunchInitiationIntentEvent ||
							event instanceof nomadrealms.event.networking.HolePunchEvent ||
							event instanceof nomadrealms.event.networking.HolePunchSuccessConfirmationEvent ||
							event instanceof nomadrealms.event.networking.HolePunchSuccessAcknowledgementEvent
					)),
			new BranchStep(
					ctx -> ctx.triggerEvent() instanceof nomadrealms.event.networking.HolePunchInitiationEvent,
					new HolePunchServerStep(),
					new BranchStep(
							ctx -> ctx.triggerEvent() instanceof nomadrealms.event.networking.HolePunchInitiationInfoPackageEvent ||
									ctx.triggerEvent() instanceof nomadrealms.event.networking.HolePunchInitiationIntentEvent,
							new AddConnectionStep(),
							new UpdateConnectionStep()
					)
			)
	),

	GENERIC_EVENT(
			(event, role) -> true,
			new HandleGenericEventStep()
	);

	private final BiPredicate<SyncedEvent, NetworkRole> trigger;
	private final NetworkStep step;

	NetworkFlowTemplate(BiPredicate<SyncedEvent, NetworkRole> trigger, NetworkStep step) {
		this.trigger = trigger;
		this.step = step;
	}

	public Optional<NetworkFlow> tryTrigger(SyncedEvent event, PacketAddress address, NetworkRole role) {
		if (trigger.test(event, role)) {
			NetworkFlow flow = step.createFlow();
			flow.sourceAddress(address);
			flow.triggerEvent(event);
			return Optional.of(flow);
		}
		return Optional.empty();
	}

}
