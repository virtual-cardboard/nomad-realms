package engine.networking.flow.experimental;

import engine.networking.graph.NetworkRole;
import nomadrealms.event.networking.SyncedEvent;

public class FromToDefinition {


	private final BlockDefinition previousBlock;
	private final NetworkRole source;
	private final NetworkRole target;

	public FromToDefinition(BlockDefinition previousBlock, NetworkRole source, NetworkRole target) {
		this.previousBlock = previousBlock;
		this.source = source;
		this.target = target;
	}

	public <T extends SyncedEvent> BlockDefinition<T> send(Class<T> eventClass) {
		return new BlockDefinition<>(previousBlock, source, target, eventClass);
	}


}
