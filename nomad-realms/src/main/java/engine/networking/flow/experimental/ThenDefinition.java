package engine.networking.flow.experimental;

import engine.networking.graph.NetworkRole;

public class ThenDefinition {

	private BlockDefinition block;

	public ThenDefinition(BlockDefinition block) {
		this.block = block;
	}


	public FromDefinition from(NetworkRole role) {
		return new FromDefinition(block, role);
	}

}
