package context.game.input;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

import java.util.function.Function;

import common.event.GameEvent;
import context.input.event.MousePressedInputEvent;

public class CancelCardMousePressedFunction implements Function<MousePressedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public CancelCardMousePressedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent t) {
		if (t.button() != GLFW_MOUSE_BUTTON_RIGHT || inputContext.cardWaitingForTarget == null) {
			return null;
		}
		inputContext.cardWaitingForTarget.setLockTargetPos(false);
		inputContext.cardWaitingForTarget = null;
		inputContext.visuals.dashboardGui().hand().resetTargetPositions(inputContext.visuals.rootGui().dimensions());
		return null;
	}

}
