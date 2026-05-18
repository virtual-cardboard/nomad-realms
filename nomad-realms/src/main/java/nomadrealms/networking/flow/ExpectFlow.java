package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

public class ExpectFlow extends NetworkFlow {

	private final Class<? extends SyncedEvent> eventClass;
	private SyncedEvent capturedEvent;
	private final PacketAddress targetAddress;

	public ExpectFlow(Class<? extends SyncedEvent> eventClass, PacketAddress targetAddress) {
		this.eventClass = eventClass;
		this.targetAddress = targetAddress;
	}

	@Override
	public boolean consume(SyncedEvent event, NetworkFlowContext context) {
		if (capturedEvent == null && eventClass.isInstance(event)) {
			if (context != null && targetAddress != null && !targetAddress.equals(context.source())) {
				return false;
			}
			capturedEvent = event;
			return true;
		}
		return false;
	}

	@Override
	public boolean isCompleted() {
		return capturedEvent != null;
	}

	public SyncedEvent capturedEvent() {
		return capturedEvent;
	}

}
