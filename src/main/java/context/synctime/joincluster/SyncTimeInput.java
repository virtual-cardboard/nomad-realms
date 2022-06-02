package context.synctime.joincluster;

import context.input.GameInput;
import networking.protocols.NomadRealmsProtocolDecoder;

public final class SyncTimeInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new NomadRealmsProtocolDecoder());
	}

}
