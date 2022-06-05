package context.synctime;

import static java.lang.System.currentTimeMillis;

import context.input.GameInput;
import networking.protocols.NomadRealmsProtocolDecoder;

public final class SyncTimeInput extends GameInput {

	@Override
	protected void init() {
		SyncTimeData data = (SyncTimeData) context().data();
		addPacketReceivedFunction(new NomadRealmsProtocolDecoder().andThen(e -> {
			data.setT3(currentTimeMillis());
			return e;
		}));
	}

}
