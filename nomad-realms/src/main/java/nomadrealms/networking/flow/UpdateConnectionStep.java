package nomadrealms.networking.flow;

import nomadrealms.event.networking.HolePunchEvent;
import nomadrealms.event.networking.HolePunchSuccessConfirmationEvent;
import nomadrealms.event.networking.HolePunchSuccessAcknowledgementEvent;
import nomadrealms.networking.ConnectionState;

public class UpdateConnectionStep implements NetworkStep {

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
				if (triggerEvent() instanceof HolePunchEvent) {
					HolePunchEvent event = (HolePunchEvent) triggerEvent();
					context.networkGraph().getConnection(event.nonce()).ifPresent(connection -> {
						if (connection.state() == ConnectionState.LISTENING) {
							connection.state(ConnectionState.RECEIVING);
							connection.targetAddress(sourceAddress());
							connection.player().address(sourceAddress());
						}
					});
				} else if (triggerEvent() instanceof HolePunchSuccessConfirmationEvent) {
					HolePunchSuccessConfirmationEvent event = (HolePunchSuccessConfirmationEvent) triggerEvent();
					context.networkGraph().getConnection(event.nonce()).ifPresent(connection -> {
						connection.state(ConnectionState.HEALTHY);
						connection.targetAddress(sourceAddress());
						connection.player().address(sourceAddress());
						context.networkGraph().send(new HolePunchSuccessAcknowledgementEvent(event.nonce()), sourceAddress());
					});
				} else if (triggerEvent() instanceof HolePunchSuccessAcknowledgementEvent) {
					HolePunchSuccessAcknowledgementEvent event = (HolePunchSuccessAcknowledgementEvent) triggerEvent();
					context.networkGraph().getConnection(event.nonce()).ifPresent(connection -> {
						connection.state(ConnectionState.HEALTHY);
						connection.targetAddress(sourceAddress());
						connection.player().address(sourceAddress());
					});
				}
				complete(true);
			}
		};
	}

}
