package event;

import engine.common.event.async.AsyncGameEvent;

public class NomadRealmsAsyncEvent extends AsyncGameEvent {

	public NomadRealmsAsyncEvent(long scheduledTick) {
		super(scheduledTick);
	}

}
