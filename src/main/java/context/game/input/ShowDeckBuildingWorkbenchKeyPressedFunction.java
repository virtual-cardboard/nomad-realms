package context.game.input;

import java.awt.event.KeyEvent;
import java.util.function.Function;

import common.event.GameEvent;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.deckbuilding.DeckBuildingGui;
import context.input.event.KeyPressedInputEvent;

public class ShowDeckBuildingWorkbenchKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private NomadsGameVisuals visuals;

	public ShowDeckBuildingWorkbenchKeyPressedFunction(NomadsGameVisuals visuals) {
		this.visuals = visuals;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent t) {
		if (t.code() == KeyEvent.VK_D) {
			DeckBuildingGui deckBuildingGui = visuals.deckBuildingGui();
			deckBuildingGui.setEnabled(!deckBuildingGui.isEnabled());
		}
		return null;
	}

}
