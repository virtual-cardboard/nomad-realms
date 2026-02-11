package engine.common.time;

public abstract class TerminateableRunnable implements Runnable {

	private boolean isDone = false;

	@Override
	public void run() {
		while (!isDone) {
			doRun();
			Thread.yield();
		}
	}

	protected abstract void doRun();

	public void terminate() {
		isDone = true;
		doTerminate();
	}

	protected void doTerminate() {

	}

}
