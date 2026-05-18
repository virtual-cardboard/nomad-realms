package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.List;
import nomadrealms.event.networking.SyncedEvent;

public class SequenceFlow extends NetworkFlow {

	private final List<NetworkFlowTemplateNode> nodes;
	private int currentIndex = 0;
	private NetworkFlow currentFlow;
	private final NetworkRole role;
	private SyncedEvent lastEvent;
	private final PacketAddress initialAddress;

	public SequenceFlow(List<NetworkFlowTemplateNode> nodes, SyncedEvent triggerEvent, NetworkRole role, PacketAddress address) {
		this.nodes = nodes;
		this.role = role;
		this.lastEvent = triggerEvent;
		this.initialAddress = address;
		this.currentFlow = nodes.get(0).createFlow(triggerEvent, role, address);
	}

	@Override
	public boolean consume(SyncedEvent event, NetworkFlowContext context) {
		if (currentFlow != null && currentFlow.consume(event, context)) {
			if (currentFlow instanceof ExpectFlow) {
				lastEvent = ((ExpectFlow) currentFlow).capturedEvent();
			}
			return true;
		}
		return false;
	}

	@Override
	public void update(NetworkFlowContext context) {
		if (currentFlow == null) {
			return;
		}
		currentFlow.update(context);
		if (currentFlow.isCompleted()) {
			currentIndex++;
			if (currentIndex < nodes.size()) {
				currentFlow = nodes.get(currentIndex).createFlow(lastEvent, role, initialAddress);
				currentFlow.update(context);
			} else {
				currentFlow = null;
			}
		}
	}

	@Override
	public boolean isCompleted() {
		return currentFlow == null;
	}

}
