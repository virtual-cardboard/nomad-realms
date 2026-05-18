package nomadrealms.networking.flow;

import java.util.List;
import java.util.stream.Collectors;
import nomadrealms.event.networking.bootstrap.OnlinePlayersListEvent;
import nomadrealms.user.Player;

public class GetOnlinePlayersStep implements NetworkStep {

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
				List<Player> otherPlayers = context.onlinePlayers().stream()
						.filter(p -> !p.address().equals(sourceAddress()))
						.collect(Collectors.toList());
				context.networkGraph().send(new OnlinePlayersListEvent(otherPlayers), sourceAddress());
				complete(true);
			}
		};
	}

}
