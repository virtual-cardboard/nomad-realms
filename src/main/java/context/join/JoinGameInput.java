package context.join;

import context.input.GameInput;
import networking.protocols.NomadRealmsProtocolDecoder;

public final class JoinGameInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new NomadRealmsProtocolDecoder());
	}

}
