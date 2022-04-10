package context.game.input.world;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

import java.util.function.Function;

import context.input.event.MousePressedInputEvent;
import engine.common.event.GameEvent;

public class CancelCardMousePressedFunction implements Function<MousePressedInputEvent, GameEvent> {

	private NomadsInputWorldInfo inputInfo;

	public CancelCardMousePressedFunction(NomadsInputWorldInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent t) {
		if (t.button() != GLFW_MOUSE_BUTTON_RIGHT || inputInfo.cardWaitingForTarget == null) {
			return null;
		}
		inputInfo.cardWaitingForTarget.setLockTargetPos(false);
		inputInfo.cardWaitingForTarget = null;
		inputInfo.visuals.dashboardGui().hand().resetTargetPositions(inputInfo.visuals.rootGui().dimensions(), inputInfo.settings);
		return null;
	}

}
