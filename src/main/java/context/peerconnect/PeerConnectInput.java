package context.peerconnect;

import context.input.GameInput;
import networking.protocols.NomadRealmsProtocolDecoder;

public class PeerConnectInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new NomadRealmsProtocolDecoder());
	}

}
