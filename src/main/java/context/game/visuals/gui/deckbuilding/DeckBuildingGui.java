package context.game.visuals.gui.deckbuilding;

import static context.visuals.colour.Colour.rgb;

import java.util.HashMap;
import java.util.Map;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.RelativePositionConstraint;
import model.card.CardCollection;
import model.card.CollectionCard;

public class DeckBuildingGui extends Gui {

	private Map<CollectionCard, CollectionCardGui> cardGuis = new HashMap<>();

	private RectangleRenderer rectangleRenderer;

	private CardCollectionGui cardCollectionGui;
	private CollectionDeckGui collectionDeckGui;

	public DeckBuildingGui(CardCollection collection, ResourcePack rp, NomadsSettings s) {
		this.rectangleRenderer = rp.getRenderer("rectangle", RectangleRenderer.class);
		setWidth(new RelativeDimensionConstraint(0.8f));
		setHeight(new RelativeDimensionConstraint(0.8f));
		setPosX(new RelativePositionConstraint(0.1f));
		setPosY(new RelativePositionConstraint(0.1f));
		cardCollectionGui = new CardCollectionGui(collection, rectangleRenderer);
		collectionDeckGui = new CollectionDeckGui(rectangleRenderer, s);
		addChild(cardCollectionGui);
		addChild(collectionDeckGui);
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		rectangleRenderer.render(x, y, width, height, rgb(135, 117, 59));
	}

	public CollectionCardGui getCardGui(CollectionCard card) {
		return cardGuis.get(card);
	}

	public CollectionCardGui putCardGui(CollectionCard key, CollectionCardGui value) {
		return cardGuis.put(key, value);
	}

	public CollectionCardGui removeCardGui(CollectionCard card) {
		return cardGuis.remove(card);
	}

}
