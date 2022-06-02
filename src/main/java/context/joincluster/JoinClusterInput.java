package context.joincluster;

import context.input.GameInput;
import networking.protocols.NomadRealmsProtocolDecoder;

public final class JoinClusterInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new NomadRealmsProtocolDecoder());
	}

}
