package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

public interface NetworkFlowTemplateNode {

	boolean canTrigger(SyncedEvent event, NetworkRole role);

	NetworkFlow createFlow(SyncedEvent event, NetworkRole role, PacketAddress address);

}
