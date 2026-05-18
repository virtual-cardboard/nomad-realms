package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.function.Predicate;
import nomadrealms.event.networking.SyncedEvent;

public class BranchFlow extends NetworkFlow {

	private final Predicate<FlowContext> condition;
	private final NetworkStep ifTrue;
	private final NetworkStep ifFalse;
	private NetworkFlow currentFlow;

	public BranchFlow(Predicate<FlowContext> condition, NetworkStep ifTrue, NetworkStep ifFalse) {
		this.condition = condition;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
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
			if (currentFlow.isCompleted()) {
				complete(currentFlow.isSucceeded());
			}
		}
	}

	@Override
	public void update(FlowContext context) {
		if (currentFlow == null) {
			if (condition.test(context)) {
				currentFlow = ifTrue.createFlow();
			} else if (ifFalse != null) {
				currentFlow = ifFalse.createFlow();
			} else {
				complete(true);
				return;
			}
			currentFlow.sourceAddress(sourceAddress());
			currentFlow.triggerEvent(triggerEvent());
			currentFlow.update(context);
		}
		currentFlow.update(context);
		if (currentFlow.isCompleted()) {
			complete(currentFlow.isSucceeded());
		}
	}

}
