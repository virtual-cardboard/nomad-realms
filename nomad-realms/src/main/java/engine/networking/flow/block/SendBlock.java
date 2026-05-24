package engine.networking.flow.block;

import static java.util.Collections.emptyList;

import java.util.List;
import nomadrealms.networking.NomadsNetworkGraph;

public class SendBlock extends FlowBlock {

	@Override
	public List<FlowBlock> advance(NomadsNetworkGraph graph) {

		return emptyList();
	}

}
