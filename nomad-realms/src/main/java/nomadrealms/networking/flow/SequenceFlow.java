package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.List;
import nomadrealms.event.networking.SyncedEvent;

public class SequenceFlow extends NetworkFlow {

	private final List<NetworkStep> steps;
	private int currentStepIndex = 0;
	private NetworkFlow currentFlow;

	public SequenceFlow(List<NetworkStep> steps) {
		this.steps = steps;
		if (!steps.isEmpty()) {
			this.currentFlow = steps.get(0).createFlow();
		} else {
			complete(true);
		}
	}

	@Override
	public void sourceAddress(PacketAddress sourceAddress) {
		super.sourceAddress(sourceAddress);
		if (currentFlow != null) {
			currentFlow.sourceAddress(sourceAddress);
		}
	}

	@Override
	public void triggerEvent(SyncedEvent triggerEvent) {
		super.triggerEvent(triggerEvent);
		if (currentFlow != null) {
			currentFlow.triggerEvent(triggerEvent);
		}
	}

	@Override
	public boolean test(SyncedEvent event, PacketAddress address) {
		return currentFlow != null && currentFlow.test(event, address);
	}

	@Override
	public void handle(SyncedEvent event, PacketAddress address, FlowContext context) {
		if (currentFlow != null) {
			currentFlow.handle(event, address, context);
			advanceIfNecessary(context);
		}
	}

	@Override
	public void update(FlowContext context) {
		if (currentFlow != null) {
			currentFlow.update(context);
			advanceIfNecessary(context);
		}
	}

	private void advanceIfNecessary(FlowContext context) {
		while (currentFlow != null && currentFlow.isCompleted()) {
			if (!currentFlow.isSucceeded()) {
				complete(false);
				return;
			}
			currentStepIndex++;
			if (currentStepIndex < steps.size()) {
				currentFlow = steps.get(currentStepIndex).createFlow();
				currentFlow.sourceAddress(sourceAddress());
				currentFlow.triggerEvent(triggerEvent());
				currentFlow.update(context);
			} else {
				currentFlow = null;
				complete(true);
			}
		}
	}

}
