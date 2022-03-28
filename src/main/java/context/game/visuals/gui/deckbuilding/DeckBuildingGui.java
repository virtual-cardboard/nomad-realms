package context.game.visuals.gui.deckbuilding;

import static context.visuals.colour.Colour.rgb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.game.NomadsGameData;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import model.card.CardCollection;
import model.card.CollectionCard;

public class DeckBuildingGui extends Gui {

	private Map<CollectionCard, CollectionCardGui> cardGuis = new HashMap<>();

	private RectangleRenderer rectangleRenderer;

	public DeckBuildingGui(CardCollection collection, ResourcePack rp, NomadsSettings s, NomadsGameData data) {
		this.rectangleRenderer = rp.getRenderer("rectangle", RectangleRenderer.class);
		setWidth(new RelativeDimensionConstraint(0.8f));
		setHeight(new RelativeDimensionConstraint(0.92f));
		setPosX(new CenterPositionConstraint(width()));
		setPosY(new CenterPositionConstraint(height()));
		CollectionGui collectionGui = new CollectionGui(collection, rectangleRenderer, data);
		CollectionDeckGui collectionDeckGui = new CollectionDeckGui(rectangleRenderer, s);
		addChild(collectionGui);
		addChild(collectionDeckGui);
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		rectangleRenderer.render(x, y, width, height, rgb(135, 117, 59));
	}

	public void createCardGuis(ResourcePack rp, NomadsSettings settings) {
		collectionGui().createCardGuis(rp, settings);
		collectionDeckGui();
	}

	public void resetTargetPositions(NomadsSettings settings) {
		collectionGui().resetTargetPositions(settings);
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

	public Collection<CollectionCardGui> cardGuis() {
		return cardGuis.values();
	}

	public CollectionGui collectionGui() {
		return (CollectionGui) getChildren().get(0);
	}

	public CollectionDeckGui collectionDeckGui() {
		return (CollectionDeckGui) getChildren().get(1);
	}

}
