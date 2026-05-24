package engine.networking.flow.experimental;

import engine.networking.graph.NetworkRole;
import nomadrealms.event.networking.SyncedEvent;

public class BlockDefinition<T extends SyncedEvent> {

	private final BlockDefinition previousBlock;
	NetworkRole source;
	NetworkRole target;
	private final Class<T> eventClass;

	public BlockDefinition(BlockDefinition previousBlock, NetworkRole source, NetworkRole target, Class<T> eventClass) {
		this.previousBlock = previousBlock;
		this.source = source;
		this.target = target;
		this.eventClass = eventClass;
	}

	public ThenDefinition then() {
		return new ThenDefinition(this);
	}

}
