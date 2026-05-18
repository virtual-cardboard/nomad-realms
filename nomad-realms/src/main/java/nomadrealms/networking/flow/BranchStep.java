package nomadrealms.networking.flow;

import java.util.function.Predicate;

public class BranchStep implements NetworkStep {

	private final Predicate<FlowContext> condition;
	private final NetworkStep ifTrue;
	private final NetworkStep ifFalse;

	public BranchStep(Predicate<FlowContext> condition, NetworkStep ifTrue, NetworkStep ifFalse) {
		this.condition = condition;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
	}

	@Override
	public NetworkFlow createFlow() {
		return new BranchFlow(condition, ifTrue, ifFalse);
	}

}
