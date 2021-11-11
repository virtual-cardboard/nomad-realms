package context.game.visuals.gui;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2f;
import context.visuals.gui.Gui;

public abstract class CardZoneGui extends Gui {

	private List<CardGui> cardGuis = new ArrayList<>();

	public List<CardGui> cardGuis() {
		return cardGuis;
	}

	public void addCardGui(CardGui cardGui) {
		cardGuis.add(cardGui);
		addChild(cardGui);
	}

	public CardGui removeCardGui(int index) {
		CardGui removed = cardGuis.remove(index);
		removed.remove();
		return removed;
	}

	public void updateCardPositions() {
		for (CardGui cardGui : cardGuis) {
			cardGui.updatePos();
		}
	}

	public void resetTargetPositions(Vector2f screenDimensions) {
		Vector2f centerPos = centerPos(screenDimensions);
		for (int i = 0; i < cardGuis.size(); i++) {
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
