package context.game;

import static common.event.NetworkEvent.fromPacket;

import common.GameInputEventHandler;
import context.input.GameInput;

public class NomadsGameInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new GameInputEventHandler<>((event) -> {
			return fromPacket(event.getModel());
		}));
	}

}
