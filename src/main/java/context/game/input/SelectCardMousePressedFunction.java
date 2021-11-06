package context.game.input;

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
		CardGui hovered = inputContext.hoveredCardGui();
		inputContext.unhoverAllCardGuis();
		if (hovered != null && inputContext.targetingType == null) {
			hovered.hover();
			inputContext.selectedCardGui = hovered;
			inputContext.cardMouseOffset = hovered.posdim().pos().negate().add(inputContext.cursor.coordinates());
			hovered.setLockPos(true);
		}
		return null;
	}

}
