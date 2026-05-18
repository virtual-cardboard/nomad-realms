package nomadrealms.networking.flow;

public class RetryStep implements NetworkStep {

	private final NetworkStep step;
	private final int maxRetries;

	public RetryStep(NetworkStep step, int maxRetries) {
		this.step = step;
		this.maxRetries = maxRetries;
	}

	@Override
	public NetworkFlow createFlow() {
		return new RetryFlow(step, maxRetries);
	}

}
