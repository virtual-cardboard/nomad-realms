package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.event.networking.SyncedEvent;

public class SequenceNode implements NetworkFlowTemplateNode {

	private final List<NetworkFlowTemplateNode> nodes;

	public SequenceNode(List<NetworkFlowTemplateNode> nodes) {
		this.nodes = nodes;
	}

	@Override
	public boolean canTrigger(SyncedEvent event, NetworkRole role) {
		return !nodes.isEmpty() && nodes.get(0).canTrigger(event, role);
	}

	@Override
	public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
		return new SequenceFlow(nodes, event, role, address);
	}

}
