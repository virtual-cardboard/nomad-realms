package context.game.input;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.function.Function;

import common.event.GameEvent;
import context.game.visuals.gui.CardGui;
import context.input.event.MousePressedInputEvent;

public class SelectCardMousePressedFunction implements Function<MousePressedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public SelectCardMousePressedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent event) {
		if (inputContext.cardWaitingForTarget != null || event.button() != GLFW_MOUSE_BUTTON_LEFT) {
			return null;
		}
		CardGui hovered = inputContext.hoveredCardGui();
		inputContext.unhoverAllCardGuis();
		if (hovered != null && inputContext.cardWaitingForTarget == null) {
			hovered.hover();
			inputContext.selectedCardGui = hovered;
			inputContext.cardMouseOffset = hovered.posdim().pos().negate().add(inputContext.cursor.pos().toVec2f());
			hovered.setLockPos(true);
		}
		return null;
	}

}
