package context.game.input.world;

import java.util.function.Function;

import context.game.visuals.gui.dashboard.WorldCardGui;
import context.input.event.MouseMovedInputEvent;
import engine.common.event.GameEvent;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;

public class MoveSelectedCardMouseMovedFunction implements Function<MouseMovedInputEvent, GameEvent> {

	private NomadsInputWorldInfo inputInfo;

	public MoveSelectedCardMouseMovedFunction(NomadsInputWorldInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MouseMovedInputEvent t) {
		WorldCardGui selectedCardGui = inputInfo.selectedCardGui;
		if (selectedCardGui != null) {
			if (inputInfo.visuals.dashboardGui().hand().contains(selectedCardGui)) {
				selectedCardGui.setCenterPos(inputInfo.cursor.pos().toVec2f().sub(inputInfo.cardMouseOffset));
				Vector2f velocity = inputInfo.cursor.velocity().toVec2f();
				Vector3f perpendicular = new Vector3f(velocity.y(), -velocity.x(), 0);
				float rotateAmount = Math.min(40, velocity.length() * 0.3f);
				selectedCardGui.setCurrentOrientation(selectedCardGui.currentOrientation().rotateBy(perpendicular, rotateAmount));
			} else {
				inputInfo.selectedCardGui = null;
			}
		}
		return null;
	}

}
