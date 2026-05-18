package nomadrealms.networking.flow;

import java.util.function.BiPredicate;
import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

public class ExpectStep implements NetworkStep {

	private final BiPredicate<SyncedEvent, PacketAddress> predicate;
	private final long timeoutMs;

	public ExpectStep(BiPredicate<SyncedEvent, PacketAddress> predicate, long timeoutMs) {
		this.predicate = predicate;
		this.timeoutMs = timeoutMs;
	}

	@Override
	public NetworkFlow createFlow() {
		return new ExpectFlow(predicate, timeoutMs);
	}

}
