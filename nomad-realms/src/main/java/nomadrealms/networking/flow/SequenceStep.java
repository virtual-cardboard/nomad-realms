package nomadrealms.networking.flow;

import java.util.Arrays;
import java.util.List;

public class SequenceStep implements NetworkStep {

	private final List<NetworkStep> steps;

	public SequenceStep(NetworkStep... steps) {
		this.steps = Arrays.asList(steps);
	}

	@Override
	public NetworkFlow createFlow() {
		return new SequenceFlow(steps);
	}

}
