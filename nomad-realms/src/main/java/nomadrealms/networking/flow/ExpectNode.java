package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

public class ExpectNode implements NetworkFlowTemplateNode {

	private final Class<? extends SyncedEvent> eventClass;

	public ExpectNode(Class<? extends SyncedEvent> eventClass) {
		this.eventClass = eventClass;
	}

	@Override
	public boolean canTrigger(SyncedEvent event, NetworkRole role) {
		return eventClass.isInstance(event);
	}

	@Override
	public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
		ExpectFlow flow = new ExpectFlow(eventClass, address);
		flow.consume(event, null);
		return flow;
	}

}
