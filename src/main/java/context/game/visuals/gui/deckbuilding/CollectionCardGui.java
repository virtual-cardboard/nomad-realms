package context.game.visuals.gui.deckbuilding;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.gui.CardGui;
import model.card.CollectionCard;

/**
 * The GUI of a card in the player's collection.
 * 
 * @author Jay
 */
public class CollectionCardGui extends CardGui {

	private CollectionCard card;

	public CollectionCardGui(CollectionCard card, ResourcePack resourcePack) {
		super(resourcePack, card.card());
		this.card = card;
	}

	public void render(GLContext glContext, NomadsSettings s) {
		render(glContext, s, card.name(), card.text(), card.cost());
	}

	public CollectionCard card() {
		return card;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + card;
	}

}
