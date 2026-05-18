package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.function.Function;
import nomadrealms.event.networking.SyncedEvent;

public class SendFlow extends NetworkFlow {

	private final Function<SyncedEvent, SyncedEvent> eventProvider;
	private final SyncedEvent triggerEvent;
	private final PacketAddress targetAddress;
	private boolean sent = false;

	public SendFlow(Function<SyncedEvent, SyncedEvent> eventProvider, SyncedEvent triggerEvent, PacketAddress targetAddress) {
		this.eventProvider = eventProvider;
		this.triggerEvent = triggerEvent;
		this.targetAddress = targetAddress;
	}

	@Override
	public boolean consume(SyncedEvent event, NetworkFlowContext context) {
		return false;
	}

	@Override
	public void update(NetworkFlowContext context) {
		if (!sent) {
			PacketAddress dest = (targetAddress != null) ? targetAddress : context.source();
			context.networkNode().send(eventProvider.apply(triggerEvent), dest);
			sent = true;
		}
	}

	@Override
	public boolean isCompleted() {
		return sent;
	}

}
