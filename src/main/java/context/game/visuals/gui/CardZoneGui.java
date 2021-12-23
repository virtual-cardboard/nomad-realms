package context.game.visuals.gui;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2f;
import context.visuals.gui.Gui;

public abstract class CardZoneGui extends Gui {

	public List<CardGui> cardGuis() {
		List<CardGui> cardGuis = new ArrayList<>(getChildren().size());
		for (int i = 0; i < getChildren().size(); i++) {
			cardGuis.add((CardGui) getChildren().get(i));
		}
		return cardGuis;
	}

	public CardGui cardGui(int index) {
		return (CardGui) getChildren().get(index);
	}

	public void addCardGui(CardGui cardGui) {
		addChild(cardGui);
		((CardDashboardGui) parent()).putCardGui(cardGui.card(), cardGui);
	}

	public void addCardGui(int i, CardGui cardGui) {
		addChild(i, cardGui);
		((CardDashboardGui) parent()).putCardGui(cardGui.card(), cardGui);
	}

	public void removeCardGui(CardGui cardGui) {
		((CardDashboardGui) parent()).removeCardGui(cardGui.card());
		cardGui.remove();
	}

	public CardGui removeCardGui(int index) {
		CardGui cardGui = (CardGui) getChildren().get(index);
		((CardDashboardGui) parent()).removeCardGui(cardGui.card());
		cardGui.remove();
		return cardGui;
	}

	public void updateCardPositions() {
		for (CardGui cardGui : cardGuis()) {
			cardGui.updatePos();
		}
	}

	public void resetTargetPositions(Vector2f screenDimensions) {
		Vector2f centerPos = centerPos(screenDimensions);
		List<CardGui> cardGuis = cardGuis();
		for (int i = 0; i < cardGuis.size(); i++) {
			if (cardGuis.get(i).lockedTargetPos()) {
				continue;
			}
			cardGuis.get(i).setTargetPos(centerPos.x, centerPos.y);
		}
	}

	public Vector2f topLeftPos(Vector2f screenDimensions) {
		return new Vector2f(getPosX().calculateValue(0, screenDimensions.x), getPosY().calculateValue(0, screenDimensions.y));
	}

	public Vector2f centerPos(Vector2f screenDimensions) {
		Vector2f cornerToCenter = new Vector2f(getWidth().calculateValue(0, screenDimensions.x), getHeight().calculateValue(0, screenDimensions.y));
		return topLeftPos(screenDimensions).add(cornerToCenter.scale(0.5f));
	}

}
