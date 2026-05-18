package nomadrealms.networking.flow;

import nomadrealms.event.networking.bootstrap.OnlinePlayersListEvent;

public class UpdateOnlinePlayersStep implements NetworkStep {

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
				OnlinePlayersListEvent event = (OnlinePlayersListEvent) triggerEvent();
				context.onlinePlayers().clear();
				context.onlinePlayers().addAll(event.players());
				complete(true);
			}
		};
	}

}
