package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.function.BiPredicate;
import nomadrealms.event.networking.SyncedEvent;

public class ExpectFlow extends NetworkFlow {

	private final BiPredicate<SyncedEvent, PacketAddress> predicate;
	private final long timeoutMs;
	private final long startTime;

	public ExpectFlow(BiPredicate<SyncedEvent, PacketAddress> predicate, long timeoutMs) {
		this.predicate = predicate;
		this.timeoutMs = timeoutMs;
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public boolean test(SyncedEvent event, PacketAddress address) {
		return predicate.test(event, address);
	}

	@Override
	public void handle(SyncedEvent event, PacketAddress address, FlowContext context) {
		complete(true);
	}

	@Override
	public void update(FlowContext context) {
		if (System.currentTimeMillis() - startTime > timeoutMs) {
			complete(false);
		}
	}

}
