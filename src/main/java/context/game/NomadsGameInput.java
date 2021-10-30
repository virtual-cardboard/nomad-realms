package context.game;

import static common.event.NetworkEvent.fromPacket;

import common.GameInputEventHandler;
import context.input.GameInput;
import context.visuals.GameVisuals;

public class NomadsGameInput extends GameInput {

	@Override
	protected void init() {
		addPacketReceivedFunction(new GameInputEventHandler<>(event -> {
			return fromPacket(event.model());
		}));
		addMouseReleasedFunction(new GameInputEventHandler<>(event -> {
			GameVisuals visuals = context().visuals();
			return null;
		}));
	}

}
