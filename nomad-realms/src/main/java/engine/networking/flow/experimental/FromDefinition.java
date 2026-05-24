package engine.networking.flow.experimental;

import engine.networking.graph.NetworkRole;

public class FromDefinition {

	private BlockDefinition previousBlock = null;
	private NetworkRole role;

	private FromDefinition(NetworkRole role) {
		this.role = role;
	}

	protected FromDefinition(BlockDefinition previousBlock, NetworkRole role) {
		this.previousBlock = previousBlock;
		this.role = role;
	}

	public FromToDefinition to(NetworkRole role) {
		return new FromToDefinition(previousBlock, this.role, role);
	}

	public static FromDefinition from(NetworkRole role) {
		return new FromDefinition(role);
	}
}
