package context.game.visuals.gui;

import java.util.ArrayList;
import java.util.List;

import context.visuals.gui.Gui;

public abstract class CardZoneGui extends Gui {

	private List<CardGui> cardGuis = new ArrayList<>();

	public List<CardGui> cardGuis() {
		return cardGuis;
	}

}
