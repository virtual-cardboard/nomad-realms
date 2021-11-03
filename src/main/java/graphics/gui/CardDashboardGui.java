package graphics.gui;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2f;
import context.ResourcePack;
import context.visuals.colour.Colour;
import context.visuals.gui.ColourGui;
import context.visuals.gui.Gui;
import context.visuals.gui.InvisibleGui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;

public final class CardDashboardGui extends InvisibleGui {

	private List<CardGui> cardGuis = new ArrayList<>(8);
	private ColourGui cardHolder;

	public CardDashboardGui(ResourcePack resourcePack) {
		setWidth(new RelativeDimensionConstraint(1));
		setHeight(new RelativeDimensionConstraint(1));
		setPosX(new PixelPositionConstraint(0));
		setPosY(new PixelPositionConstraint(0));
		cardHolder = new ColourGui(resourcePack.defaultShaderProgram(), resourcePack.rectangleVAO(), Colour.colour(117, 96, 60));
		cardHolder.setWidth(new PixelDimensionConstraint(800));
		cardHolder.setHeight(new PixelDimensionConstraint(100));
		cardHolder.setPosX(new CenterPositionConstraint(cardHolder.getWidth()));
		cardHolder.setPosY(new BiFunctionPositionConstraint((start, end) -> end - 100));
		super.addChild(cardHolder);
	}

	@Override
	public void addChild(Gui child) {
		if (child instanceof CardGui) {
			super.addChild(child);
			cardGuis.add((CardGui) child);
			return;
		}
		throw new RuntimeException("Gui " + child.getClass().getSimpleName() + " cannot be a child of CardDashboardGui.");
	}

	public List<CardGui> cardGuis() {
		return cardGuis;
	}

	public ColourGui getCardHolder() {
		return cardHolder;
	}

	public void updateCardPositions() {
		for (CardGui cardGui : cardGuis) {
			cardGui.updatePos();
		}
	}

	public void resetTargetPositions(Vector2f screenDimensions) {
		float x = (screenDimensions.x - (cardGuis.size() - 1) * CardGui.WIDTH) * 0.5f;
		for (CardGui cardGui : cardGuis) {
			cardGui.setTargetPos(x, screenDimensions.y - CardGui.HEIGHT * 0.4f);
			x += CardGui.WIDTH;
		}
	}

	public void removeCardGui(int index) {
		cardGuis.remove(index);
	}

}