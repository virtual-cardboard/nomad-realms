package context.connect;

import static common.event.NetworkEvent.fromPacket;

import context.input.GameInput;

public class PeerConnectInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(event -> fromPacket(event.model()));
	}

}
