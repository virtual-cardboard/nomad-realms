package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

public class OnRoleNode implements NetworkFlowTemplateNode {

	private final NetworkRole targetRole;
	private final NetworkFlowTemplateNode child;

	public OnRoleNode(NetworkRole targetRole, NetworkFlowTemplateNode child) {
		this.targetRole = targetRole;
		this.child = child;
	}

	@Override
	public boolean canTrigger(SyncedEvent event, NetworkRole role) {
		return role == targetRole && child.canTrigger(event, role);
	}

	@Override
	public NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address) {
		if (role == targetRole) {
			return child.createFlow(event, role, address);
		}
		return null;
	}

}
