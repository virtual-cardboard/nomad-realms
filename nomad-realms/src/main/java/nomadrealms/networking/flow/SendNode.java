package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.function.Function;
import nomadrealms.event.networking.SyncedEvent;

public class SendNode implements NetworkFlowTemplateNode {

	private final Function<SyncedEvent, SyncedEvent> eventProvider;

	public SendNode(SyncedEvent event) {
		this.eventProvider = (e) -> event;
	}

	public SendNode(Function<SyncedEvent, SyncedEvent> eventProvider) {
		this.eventProvider = eventProvider;
	}

	@Override
	public boolean canTrigger(SyncedEvent event, NetworkRole role) {
		return false;
	}

	@Override
	public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
		return new SendFlow(eventProvider, event, address);
	}

}
