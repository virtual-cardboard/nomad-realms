package context.game;

import context.game.input.CardTargetMousePressedFunction;
import context.game.input.DetectHoveredCardMouseMovedFunction;
import context.game.input.DetectPlayedCardMouseReleasedFunction;
import context.game.input.NomadsGameInputContext;
import context.game.input.ResetCardPositionsFrameResizedFunction;
import context.game.input.SelectCardMousePressedFunction;
import context.game.input.SwitchNomadKeyPressedFunction;
import context.game.input.SwitchNomadKeyPressedPredicate;
import context.input.GameInput;
import event.network.NomadRealmsNetworkEvent;

public class NomadsGameInput extends GameInput {

	private NomadsGameInputContext inputContext = new NomadsGameInputContext();

	@Override
	protected void init() {
		inputContext.init((NomadsGameVisuals) context().visuals(), (NomadsGameData) context().data(), cursor());
		addPacketReceivedFunction(event -> {
			return NomadRealmsNetworkEvent.fromPacket(event.model());
		});
		addMouseMovedFunction(new DetectHoveredCardMouseMovedFunction(inputContext));
		addMousePressedFunction(new SelectCardMousePressedFunction(inputContext));
		addMouseReleasedFunction(new DetectPlayedCardMouseReleasedFunction(inputContext));
		addMousePressedFunction(new CardTargetMousePressedFunction(inputContext));
		addKeyPressedFunction(new SwitchNomadKeyPressedPredicate(), new SwitchNomadKeyPressedFunction(inputContext), false);
		addFrameResizedFunction(new ResetCardPositionsFrameResizedFunction(inputContext));
	}

}
