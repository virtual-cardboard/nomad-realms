package context.game.input.deckbuilding;

import java.util.function.Function;

import common.event.GameEvent;
import context.game.visuals.gui.deckbuilding.CollectionCardGui;
import context.input.event.MouseMovedInputEvent;

public class DetectHoveredCollectionCardMouseMovedFunction implements Function<MouseMovedInputEvent, GameEvent> {

	private NomadsInputDeckBuildingInfo inputInfo;

	public DetectHoveredCollectionCardMouseMovedFunction(NomadsInputDeckBuildingInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MouseMovedInputEvent event) {
		if (inputInfo.selectedCardGui != null) {
			return null;
		}
		CollectionCardGui hovered = inputInfo.hoveredCardGui();
		inputInfo.unhoverAllCardGuis();
		if (shouldHover(hovered)) {
			hovered.hover(inputInfo.settings);
		}
		return null;
	}

	private boolean shouldHover(CollectionCardGui hovered) {
		return inputInfo.selectedCardGui == null && hovered != null;
	}

}
