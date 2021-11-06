package context.connect;

import static common.event.NetworkEvent.fromPacket;

import context.input.GameInput;
import context.input.GameInputEventHandler;

public class PeerConnectInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new GameInputEventHandler<>((event) -> {
			return fromPacket(event.model());
		}));
	}

}
