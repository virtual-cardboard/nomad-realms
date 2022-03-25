package context.game;

import common.event.GameEvent;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.input.*;
import context.input.GameInput;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import networking.protocols.NomadRealmsProtocolDecoder;

public class NomadsGameInput extends GameInput {

	private NomadsInputInfo inputInfo = new NomadsInputInfo();

	private Vector2i previousCursorPos;
	private boolean pressed = false;

	private NomadsGameVisuals visuals;

	@Override
	protected void init() {
		visuals = (NomadsGameVisuals) context().visuals();
		inputInfo.init(visuals, (NomadsGameData) context().data(), cursor());

//		addMousePressedFunction(this::handleMousePressed);
//		addMouseReleasedFunction(this::handleMouseReleased);
//		addMouseMovedFunction((e) -> pressed, this::handleMouseMoved, true);

		addPacketReceivedFunction(new NomadRealmsProtocolDecoder());
		addMouseMovedFunction(new DetectHoveredCardMouseMovedFunction(inputInfo));
		addMouseMovedFunction(new MoveSelectedCardMouseMovedFunction(inputInfo));
		addMousePressedFunction(new SelectCardMousePressedFunction(inputInfo));
		addMouseReleasedFunction(new DetectPlayedCardMouseReleasedFunction(inputInfo));
		addMousePressedFunction(new CardTargetMousePressedFunction(inputInfo));
		addMousePressedFunction(new CancelCardMousePressedFunction(inputInfo));
		addKeyPressedFunction(new ShowDeckBuildingWorkbenchKeyPressedFunction(visuals));
//		addKeyPressedFunction(new SwitchNomadKeyPressedPredicate(), new SwitchNomadKeyPressedFunction(inputContext), false);
		addFrameResizedFunction(new ResetCardPositionsFrameResizedFunction(inputInfo));
	}

	private GameEvent handleMousePressed(MousePressedInputEvent event) {
		pressed = true;
		previousCursorPos = cursor().pos();
		return null;
	}

	private GameEvent handleMouseReleased(MouseReleasedInputEvent mousereleasedinputevent1) {
		pressed = false;
		return null;
	}

	private GameEvent handleMouseMoved(MouseMovedInputEvent event) {
		Vector2f prevCameraPos = visuals.camera().pos();
		Vector2i cursorPos = cursor().pos();
		visuals.camera().setPos(prevCameraPos.add(previousCursorPos).sub(cursorPos));
		previousCursorPos = cursorPos;
		return null;
	}

}
