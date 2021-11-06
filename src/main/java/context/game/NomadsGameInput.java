package context.game;

import static common.event.NetworkEvent.fromPacket;

import context.game.input.CardTargetMousePressedFunction;
import context.game.input.DetectHoveredCardMouseMovedFunction;
import context.game.input.DetectPlayedCardMouseReleasedFunction;
import context.game.input.NomadsGameInputContext;
import context.game.input.SelectCardMousePressedFunction;
import context.input.GameInput;
import context.input.GameInputEventHandler;

public class NomadsGameInput extends GameInput {

	private NomadsGameInputContext inputContext = new NomadsGameInputContext();

	@Override
	protected void init() {
		inputContext.init((NomadsGameVisuals) context().visuals(), (NomadsGameData) context().data(), cursor());
		addPacketReceivedFunction(new GameInputEventHandler<>(event -> {
			return fromPacket(event.model());
		}));
		addMouseMovedFunction(new GameInputEventHandler<>(new DetectHoveredCardMouseMovedFunction(inputContext)));
		addMousePressedFunction(new GameInputEventHandler<>(new SelectCardMousePressedFunction(inputContext)));
		addMouseReleasedFunction(new GameInputEventHandler<>(new DetectPlayedCardMouseReleasedFunction(inputContext)));
		addMousePressedFunction(new GameInputEventHandler<>(new CardTargetMousePressedFunction(inputContext)));
	}

}
