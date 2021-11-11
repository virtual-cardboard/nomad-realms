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

	public void add(CardGui cardGui) {
		cardGuis.add(cardGui);
	}

	public void removeCardGui(int index) {
		cardGuis.remove(index).remove();
	}

	public void updateCardPositions() {
		for (CardGui cardGui : cardGuis) {
			cardGui.updatePos();
		}
	}

	public abstract void resetTargetPositions(Vector2f screenDimensions);

	public Vector2f topLeftPos(Vector2f screenDimensions) {
		return new Vector2f(getPosX().calculateValue(0, screenDimensions.x), getPosY().calculateValue(0, screenDimensions.y));
	}

	public Vector2f centerPos(Vector2f screenDimensions) {
		return topLeftPos(screenDimensions).add(getWidth().calculateValue(0, screenDimensions.x), getHeight().calculateValue(0, screenDimensions.y));
	}

}
