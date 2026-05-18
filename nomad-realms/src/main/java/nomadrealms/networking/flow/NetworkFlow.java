package nomadrealms.networking.flow;

import nomadrealms.event.networking.SyncedEvent;

public abstract class NetworkFlow {

	public abstract boolean consume(SyncedEvent event, NetworkFlowContext context);

	public void update(NetworkFlowContext context) {
	}

	public abstract boolean isCompleted();

}
