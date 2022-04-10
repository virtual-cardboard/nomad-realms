package context.game.input.deckbuilding;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.function.Function;

import context.game.visuals.gui.deckbuilding.CollectionCardGui;
import context.input.event.MousePressedInputEvent;
import engine.common.event.GameEvent;

public class SelectCollectionCardMousePressedFunction implements Function<MousePressedInputEvent, GameEvent> {

	private NomadsInputDeckBuildingInfo inputInfo;

	public SelectCollectionCardMousePressedFunction(NomadsInputDeckBuildingInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent event) {
		if (event.button() != GLFW_MOUSE_BUTTON_LEFT) {
			return null;
		}
		CollectionCardGui hovered = inputInfo.hoveredCardGui();
		inputInfo.unhoverAllCardGuis();
		if (hovered != null) {
			hovered.hover(inputInfo.settings);
			inputInfo.selectedCardGui = hovered;
			inputInfo.cardMouseOffset = hovered.centerPos().negate().add(inputInfo.cursor.pos().toVec2f());
			hovered.setLockPos(true);
		}
		return null;
	}

}
