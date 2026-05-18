package nomadrealms.networking.flow;

import nomadrealms.event.networking.HolePunchInitiationInfoPackageEvent;
import nomadrealms.event.networking.HolePunchInitiationIntentEvent;
import nomadrealms.networking.Connection;
import nomadrealms.user.Player;
import java.util.Map;
import java.util.UUID;

public class AddConnectionStep implements NetworkStep {

	@Override
	public NetworkFlow createFlow() {
		return new NetworkFlow() {
			@Override
			public boolean test(nomadrealms.event.networking.SyncedEvent event, engine.context.input.networking.packet.address.PacketAddress address) {
				return false;
			}

			@Override
			public void handle(nomadrealms.event.networking.SyncedEvent event, engine.context.input.networking.packet.address.PacketAddress address, FlowContext context) {
			}

			@Override
			public void update(FlowContext context) {
				if (triggerEvent() instanceof HolePunchInitiationInfoPackageEvent) {
					HolePunchInitiationInfoPackageEvent event = (HolePunchInitiationInfoPackageEvent) triggerEvent();
					for (Map.Entry<UUID, engine.context.input.networking.packet.address.PacketAddress> entry : event.nonceToAddress().entrySet()) {
						UUID nonce = entry.getKey();
						engine.context.input.networking.packet.address.PacketAddress peerAddress = entry.getValue();
						Player peer = context.onlinePlayers().stream()
								.filter(p -> p.address().equals(peerAddress))
								.findFirst()
								.orElse(null);
						if (peer != null) {
							context.networkGraph().addConnection(new Connection(peer, nonce));
						}
					}
				} else if (triggerEvent() instanceof HolePunchInitiationIntentEvent) {
					HolePunchInitiationIntentEvent event = (HolePunchInitiationIntentEvent) triggerEvent();
					Player initiatorFromEvent = event.initiator();
					Player initiator = context.onlinePlayers().stream()
							.filter(p -> p.name().equals(initiatorFromEvent.name()))
							.findFirst()
							.orElseGet(() -> {
								context.onlinePlayers().add(initiatorFromEvent);
								return initiatorFromEvent;
							});
					context.networkGraph().addConnection(new Connection(initiator, event.nonce()));
				}
				complete(true);
			}
		};
	}

}
