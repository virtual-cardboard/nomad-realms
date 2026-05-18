package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

public abstract class NetworkFlow {

	private boolean completed = false;
	private boolean succeeded = false;

	private PacketAddress sourceAddress;
	private SyncedEvent triggerEvent;

	public abstract boolean test(SyncedEvent event, PacketAddress address);

	public abstract void handle(SyncedEvent event, PacketAddress address, FlowContext context);

	public abstract void update(FlowContext context);

	public boolean isCompleted() {
		return completed;
	}

	public boolean isSucceeded() {
		return succeeded;
	}

	protected void complete(boolean success) {
		this.completed = true;
		this.succeeded = success;
	}

	public PacketAddress sourceAddress() {
		return sourceAddress;
	}

	public void sourceAddress(PacketAddress sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public SyncedEvent triggerEvent() {
		return triggerEvent;
	}

	public void triggerEvent(SyncedEvent triggerEvent) {
		this.triggerEvent = triggerEvent;
	}

}
