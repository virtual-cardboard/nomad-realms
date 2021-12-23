package context.game;

import context.game.input.*;
import context.input.GameInput;
import networking.protocols.NomadRealmsProtocolDecoder;

public class NomadsGameInput extends GameInput {

	private NomadsGameInputInfo inputContext = new NomadsGameInputInfo();

	@Override
	protected void init() {
		inputContext.init((NomadsGameVisuals) context().visuals(), (NomadsGameData) context().data(), cursor());
		addPacketReceivedFunction(new NomadRealmsProtocolDecoder());
		addMouseMovedFunction(new DetectHoveredCardMouseMovedFunction(inputContext));
		addMousePressedFunction(new SelectCardMousePressedFunction(inputContext));
		addMouseReleasedFunction(new DetectPlayedCardMouseReleasedFunction(inputContext));
		addMousePressedFunction(new CardTargetMousePressedFunction(inputContext));
		addMousePressedFunction(new CancelCardMousePressedFunction(inputContext));
//		addKeyPressedFunction(new SwitchNomadKeyPressedPredicate(), new SwitchNomadKeyPressedFunction(inputContext), false);
		addFrameResizedFunction(new ResetCardPositionsFrameResizedFunction(inputContext));
	}

}
