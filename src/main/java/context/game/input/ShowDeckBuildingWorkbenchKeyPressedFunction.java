package context.game.input;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

import java.util.function.Function;

import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.deckbuilding.CollectionGui;
import context.game.visuals.gui.deckbuilding.DeckBuildingWorkbench;
import context.input.event.KeyPressedInputEvent;
import engine.common.event.GameEvent;

public class ShowDeckBuildingWorkbenchKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private final NomadsGameData data;
	private NomadsGameVisuals visuals;

	public ShowDeckBuildingWorkbenchKeyPressedFunction(NomadsGameData data, NomadsGameVisuals visuals) {
		this.data = data;
		this.visuals = visuals;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent t) {
		DeckBuildingWorkbench deckBuildingGui = visuals.deckBuildingGui();

		if (t.code() == 'D') {
			deckBuildingGui.setEnabled(!deckBuildingGui.isEnabled());
		}
		if (deckBuildingGui.isEnabled()) {
			if (t.code() == GLFW_KEY_LEFT) {
				CollectionGui collectionGui = deckBuildingGui.collectionGui();
				collectionGui.setPage(max(collectionGui.page() - 1, 0));
				collectionGui.recreateCardGuis(visuals.resourcePack(), data.settings());
			} else if (t.code() == GLFW_KEY_RIGHT) {
				CollectionGui collectionGui = deckBuildingGui.collectionGui();
				collectionGui.setPage(min(collectionGui.page() + 1, collectionGui.collection().numPages(collectionGui.cardsPerPage())));
				collectionGui.recreateCardGuis(visuals.resourcePack(), data.settings());
			}
		}

		return null;
	}

}
