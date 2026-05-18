package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.function.Function;
import nomadrealms.event.networking.SyncedEvent;

public class SendFlow extends NetworkFlow {

	private final Function<NetworkFlow, SyncedEvent> eventSupplier;
	private final Function<NetworkFlow, PacketAddress> addressSupplier;

	public SendFlow(Function<NetworkFlow, SyncedEvent> eventSupplier, Function<NetworkFlow, PacketAddress> addressSupplier) {
		this.eventSupplier = eventSupplier;
		this.addressSupplier = addressSupplier;
	}

	@Override
	public boolean test(SyncedEvent event, PacketAddress address) {
		return false;
	}

	@Override
	public void handle(SyncedEvent event, PacketAddress address, FlowContext context) {
	}

	@Override
	public void update(FlowContext context) {
		SyncedEvent event = eventSupplier.apply(this);
		if (event != null) {
			PacketAddress target = addressSupplier.apply(this);
			if (target == null) {
				target = sourceAddress();
			}
			context.networkGraph().send(event, target);
		}
		complete(true);
	}

}
