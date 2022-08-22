package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.visuals.gui.deckbuilding.DeckBuildingWorkbench;
import model.chain.event.BuildDeckEvent;

public class ShowDeckBuildingGuiHandler implements Consumer<BuildDeckEvent> {

	private DeckBuildingWorkbench gui;

	public ShowDeckBuildingGuiHandler(DeckBuildingWorkbench gui) {
		this.gui = gui;
	}

	@Override
	public void accept(BuildDeckEvent t) {
		gui.setEnabled(true);
	}

}
