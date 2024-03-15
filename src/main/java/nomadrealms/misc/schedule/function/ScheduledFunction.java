package nomadrealms.misc.schedule.function;

public abstract class ScheduledFunction implements Runnable {

	private final Runnable function;

	public ScheduledFunction(Runnable function) {
		this.function = function;
	}

	@Override
	public void run() {
		function.run();
	}
}
