package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.game.visuals.gui.CardGui;
import context.input.event.MouseMovedInputEvent;
import event.game.CardHoveredEvent;

public class DetectHoveredCardMouseMovedFunction implements Function<MouseMovedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public DetectHoveredCardMouseMovedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(MouseMovedInputEvent event) {
		if (inputContext.selectedCardGui != null) {
			inputContext.selectedCardGui.setPos(inputContext.cursor.coordinates().copy().sub(inputContext.cardMouseOffset));
			return null;
		}
		CardGui hovered = inputContext.hoveredCardGui();
		inputContext.unhoverAllCardGuis();
		if (hovered != null) {
			hovered.hover();
			return new CardHoveredEvent(inputContext.data.player(), hovered.card());
		}
		return null;
	}

}
