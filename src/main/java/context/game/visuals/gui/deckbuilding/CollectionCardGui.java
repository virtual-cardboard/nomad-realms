package context.game.visuals.gui.deckbuilding;

import static app.NomadsSettings.ENLARGED_CARD_SIZE_FACTOR;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.game.NomadsGameData;
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

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		NomadsGameData nomadsData = (NomadsGameData) data;
		NomadsSettings s = (nomadsData).settings();
		render(glContext, nomadsData, s, card.name(), card.text(), card.cost());
	}

	public CollectionCard card() {
		return card;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + card;
	}

	public void hover(NomadsSettings settings) {
		if (!hovered) {
			targetScale = ENLARGED_CARD_SIZE_FACTOR;
			hovered = true;
		}
	}

	public void unhover(NomadsSettings settings) {
		if (hovered) {
			targetScale = 1;
			hovered = false;
		}
	}

}
