package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

public class RetryFlow extends NetworkFlow {

	private final NetworkStep step;
	private final int maxRetries;
	private int retries = 0;
	private NetworkFlow currentFlow;

	public RetryFlow(NetworkStep step, int maxRetries) {
		this.step = step;
		this.maxRetries = maxRetries;
		this.currentFlow = step.createFlow();
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
		return currentFlow.test(event, address);
	}

	@Override
	public void handle(SyncedEvent event, PacketAddress address, FlowContext context) {
		currentFlow.handle(event, address, context);
		if (currentFlow.isCompleted() && currentFlow.isSucceeded()) {
			complete(true);
		}
	}

	@Override
	public void update(FlowContext context) {
		currentFlow.update(context);
		if (currentFlow.isCompleted()) {
			if (currentFlow.isSucceeded()) {
				complete(true);
			} else if (retries < maxRetries) {
				retries++;
				currentFlow = step.createFlow();
				currentFlow.sourceAddress(sourceAddress());
				currentFlow.triggerEvent(triggerEvent());
				currentFlow.update(context);
			} else {
				complete(false);
			}
		}
	}

}
