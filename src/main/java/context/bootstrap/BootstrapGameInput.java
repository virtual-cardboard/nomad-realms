package context.bootstrap;

import context.bootstrap.input.BootstrapResponseReader;
import context.input.GameInput;

public final class BootstrapGameInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new BootstrapResponseReader());
	}

}
