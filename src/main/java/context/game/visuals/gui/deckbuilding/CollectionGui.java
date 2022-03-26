package context.game.visuals.gui.deckbuilding;

import static context.visuals.colour.Colour.rgb;

import java.util.List;

import app.NomadsSettings;
import common.math.PosDim;
import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.game.NomadsGameData;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.constraint.position.RelativePositionConstraint;
import model.card.CardCollection;
import model.card.CollectionCard;

public class CollectionGui extends Gui {

	private RectangleRenderer rectangleRenderer;
	private CardCollection collection;
	private int page = 0;
	private int cardsPerPage = 6;

	public CollectionGui(CardCollection collection, RectangleRenderer rectangleRenderer, NomadsGameData data) {
		this.collection = collection;
		setWidth(new RelativeDimensionConstraint(0.6f));
		setHeight(new RelativeDimensionConstraint(0.9f));
		setPosX(new RelativePositionConstraint(0.1f));
		setPosY(new CenterPositionConstraint(height()));
		this.rectangleRenderer = rectangleRenderer;
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		rectangleRenderer.render(x, y, width, height, rgb(249, 198, 48));
		NomadsGameData nomadsData = (NomadsGameData) data;
		NomadsSettings settings = nomadsData.settings();
		createCardGuis(data.resourcePack());
	}

	public void resetTargetPositions(NomadsSettings settings) {
		PosDim posdim = posdim();
		int cardsPerRow = cardsPerPage / 2;
		int cardsPerCol = 2;
		float padX = (posdim.w - cardsPerRow * settings.cardWidth()) / (cardsPerRow + 1);
		float padY = (posdim.h - cardsPerCol * settings.cardHeight()) / (cardsPerCol + 1);
		List<CollectionCard> cardsOnPage = collection.cardsOnPage(page, cardsPerPage);
		for (int i = 0; i < cardsOnPage.size(); i++) {
			CollectionCard card = cardsOnPage.get(i);
			CollectionCardGui cardGui = parent().getCardGui(card);
			float x = posdim.x + padX + (i % cardsPerRow) * (settings.cardWidth() + padX);
			float y = posdim.y + padY + (i / cardsPerRow) * (settings.cardHeight() + padY);
			cardGui.setTargetPos(x + settings.cardWidth() * 0.5f, y + settings.cardHeight() * 0.5f);
		}
	}

	public void createCardGuis(ResourcePack rp) {
		for (CollectionCard card : collection.cardsOnPage(page, cardsPerPage)) {
			CollectionCardGui cardGui = parent().getCardGui(card);
			if (cardGui == null) {
				cardGui = new CollectionCardGui(card, rp);
				parent().putCardGui(card, cardGui);
				addChild(cardGui);
			}
		}
	}

	@Override
	public void setParent(Gui parent) {
		if (parent instanceof DeckBuildingGui) {
			super.setParent(parent);
		} else {
			throw new IllegalArgumentException("CardCollectionGui can only be the child of a DeckBuildingGui");
		}
	}

	@Override
	public DeckBuildingGui parent() {
		return (DeckBuildingGui) super.parent();
	}

	public int page() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
