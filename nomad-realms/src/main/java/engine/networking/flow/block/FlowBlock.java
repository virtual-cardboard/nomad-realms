package engine.networking.flow.block;

import java.util.List;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.networking.NomadsNetworkGraph;

public abstract class FlowBlock {

	protected Class<? extends SyncedEvent> expect;

	public abstract List<FlowBlock> advance(NomadsNetworkGraph graph);

}
