package nomadrealms.networking.flow;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import nomadrealms.event.networking.HolePunchInitiationInfoPackageEvent;
import nomadrealms.event.networking.HolePunchInitiationIntentEvent;
import nomadrealms.user.Player;

public class HolePunchServerStep implements NetworkStep {

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
				Player initiator = context.onlinePlayers().stream()
						.filter(player -> player.address().equals(sourceAddress()))
						.findFirst()
						.orElse(null);
				if (initiator == null) {
					complete(false);
					return;
				}

				Map<UUID, engine.context.input.networking.packet.address.PacketAddress> nonceToAddress = new HashMap<>();
				for (Player otherPlayer : context.onlinePlayers()) {
					if (otherPlayer.address().equals(sourceAddress())) {
						continue;
					}
					UUID nonce = UUID.randomUUID();
					nonceToAddress.put(nonce, otherPlayer.address());
					context.networkGraph().send(new HolePunchInitiationIntentEvent(nonce, initiator), otherPlayer.address());
				}
				context.networkGraph().send(new HolePunchInitiationInfoPackageEvent(nonceToAddress), sourceAddress());
				complete(true);
			}
		};
	}

}
