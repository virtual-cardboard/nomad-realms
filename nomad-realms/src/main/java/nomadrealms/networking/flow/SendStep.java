package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.function.Function;
import nomadrealms.event.networking.SyncedEvent;

public class SendStep implements NetworkStep {

	private final Function<NetworkFlow, SyncedEvent> eventSupplier;
	private final Function<NetworkFlow, PacketAddress> addressSupplier;

	public SendStep(Function<NetworkFlow, SyncedEvent> eventSupplier, Function<NetworkFlow, PacketAddress> addressSupplier) {
		this.eventSupplier = eventSupplier;
		this.addressSupplier = addressSupplier;
	}

	@Override
	public NetworkFlow createFlow() {
		return new SendFlow(eventSupplier, addressSupplier);
	}

}
