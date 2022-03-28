package context.game.input.deckbuilding;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.function.Function;

import common.event.GameEvent;
import context.game.visuals.gui.deckbuilding.CollectionCardGui;
import context.game.visuals.gui.deckbuilding.CollectionDeckGui;
import context.game.visuals.gui.deckbuilding.CollectionGui;
import context.game.visuals.gui.deckbuilding.DeckBuildingGui;
import context.input.event.MouseReleasedInputEvent;

public class ReleaseCollectionCardMouseReleasedFunction implements Function<MouseReleasedInputEvent, GameEvent> {

	private NomadsInputDeckBuildingInfo inputInfo;

	public ReleaseCollectionCardMouseReleasedFunction(NomadsInputDeckBuildingInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MouseReleasedInputEvent t) {
		CollectionCardGui selected = inputInfo.selectedCardGui;
		if (selected == null || t.button() != GLFW_MOUSE_BUTTON_LEFT) {
			return null;
		}
		DeckBuildingGui deckBuildingGui = inputInfo.visuals.deckBuildingGui();
		CollectionGui collectionGui = deckBuildingGui.collectionGui();
		CollectionDeckGui deckGui = deckBuildingGui.collectionDeckGui();
		if (selected.parent() == collectionGui && inputInfo.hoveringOver(deckGui)) {
			collectionGui.removeChild(selected);
			deckGui.addChild(selected);
			deckGui.resetTargetPositions(inputInfo.settings);
		} else if (selected.parent() == deckGui && inputInfo.hoveringOver(collectionGui)) {
			deckGui.removeChild(selected);
			collectionGui.addChild(selected);
			deckGui.resetTargetPositions(inputInfo.settings);
			collectionGui.resetTargetPositions(inputInfo.settings);
		}
		revertCardGui();
		return null;
	}

	private void revertCardGui() {
		inputInfo.selectedCardGui.setLockPos(false);
		inputInfo.selectedCardGui.setLockTargetPos(false);
		inputInfo.selectedCardGui.unhover(inputInfo.settings);
		inputInfo.selectedCardGui = null;
	}

}
