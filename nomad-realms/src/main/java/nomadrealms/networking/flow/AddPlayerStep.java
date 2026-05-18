package nomadrealms.networking.flow;

import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.user.Player;

public class AddPlayerStep implements NetworkStep {

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
				ConnectToServerEvent event = (ConnectToServerEvent) triggerEvent();
				Player newPlayer = new Player(event.name(), sourceAddress());
				context.onlinePlayers().add(newPlayer);
				complete(true);
			}
		};
	}

}
