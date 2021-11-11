package context.game;

import static common.event.NetworkEvent.fromPacket;

import context.game.input.*;
import context.input.GameInput;

public class NomadsGameInput extends GameInput {

	private NomadsGameInputContext inputContext = new NomadsGameInputContext();

	@Override
	protected void init() {
		inputContext.init((NomadsGameVisuals) context().visuals(), (NomadsGameData) context().data(), cursor());
		addPacketReceivedFunction(event -> {
			return fromPacket(event.model());
		});
		addMouseMovedFunction(new DetectHoveredCardMouseMovedFunction(inputContext));
		addMousePressedFunction(new SelectCardMousePressedFunction(inputContext));
		addMouseReleasedFunction(new DetectPlayedCardMouseReleasedFunction(inputContext));
		addMousePressedFunction(new CardTargetMousePressedFunction(inputContext));
	}

}
