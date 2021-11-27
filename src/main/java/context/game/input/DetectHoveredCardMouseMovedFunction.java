package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import common.math.Vector3f;
import context.game.visuals.gui.CardGui;
import context.input.event.MouseMovedInputEvent;
import event.game.playerinput.PlayerHoveredCardEvent;

public class DetectHoveredCardMouseMovedFunction implements Function<MouseMovedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public DetectHoveredCardMouseMovedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(MouseMovedInputEvent event) {
		if (inputContext.selectedCardGui != null) {
			inputContext.selectedCardGui.setPos(inputContext.cursor.pos().toVec2f().sub(inputContext.cardMouseOffset));
			Vector2f velocity = inputContext.cursor.velocity().toVec2f();
			Vector3f perpendicular = new Vector3f(velocity.y, -velocity.x, 0);
			float rotateAmount = Math.min(40, velocity.length() * 0.3f);
			inputContext.selectedCardGui.setCurrentOrientation(inputContext.selectedCardGui.currentOrientation().rotateBy(perpendicular, rotateAmount));
			return null;
		}
		CardGui hovered = inputContext.hoveredCardGui();
		inputContext.unhoverAllCardGuis();
		if (hovered != null) {
			hovered.hover();
			return new PlayerHoveredCardEvent(inputContext.data.player(), hovered.card());
		}
		return null;
	}

}
