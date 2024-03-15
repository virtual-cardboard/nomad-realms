package nomadrealms.misc.schedule.function;

public class RatioScheduledFunction extends ScheduledFunction {

	private final int period;
	private int count = 0;

	public RatioScheduledFunction(Runnable function, int period) {
		super(function);
		this.period = period;
	}

	@Override
	public void run() {
		if (count == period) {
			super.run();
			count = 0;
		} else {
			count++;
		}
	}

}
