package context.bootstrap;

import context.input.GameInput;
import networking.protocols.NomadRealmsProtocolDecoder;

public final class BootstrapGameInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new NomadRealmsProtocolDecoder());
	}

}
