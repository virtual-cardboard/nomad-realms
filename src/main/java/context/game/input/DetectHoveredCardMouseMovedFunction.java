package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import common.math.Vector3f;
import context.game.visuals.gui.CardGui;
import context.input.event.MouseMovedInputEvent;
import event.game.playerinput.PlayerHoveredCardEvent;

public class DetectHoveredCardMouseMovedFunction implements Function<MouseMovedInputEvent, GameEvent> {

	private NomadsInputInfo inputInfo;

	public DetectHoveredCardMouseMovedFunction(NomadsInputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MouseMovedInputEvent event) {
		if (inputInfo.selectedCardGui != null) {
			inputInfo.selectedCardGui.setCenterPos(inputInfo.cursor.pos().toVec2f().sub(inputInfo.cardMouseOffset));
			Vector2f velocity = inputInfo.cursor.velocity().toVec2f();
			Vector3f perpendicular = new Vector3f(velocity.y, -velocity.x, 0);
			float rotateAmount = Math.min(40, velocity.length() * 0.3f);
			inputInfo.selectedCardGui.setCurrentOrientation(inputInfo.selectedCardGui.currentOrientation().rotateBy(perpendicular, rotateAmount));
			return null;
		}
		CardGui hovered = inputInfo.hoveredCardGui();
		inputInfo.unhoverAllCardGuis();
		if (shouldHover(hovered)) {
			hovered.hover(inputInfo.settings);
			return new PlayerHoveredCardEvent(inputInfo.data.playerID(), hovered.cardID());
		}
		return null;
	}

	private boolean shouldHover(CardGui hovered) {
		return hovered != null && inputInfo.cardWaitingForTarget == null && !hovered.lockedTargetPos();
	}

}
