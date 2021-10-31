package graphics.gui;

import java.util.ArrayList;
import java.util.List;

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

	public CardDashboardGui(ResourcePack resourcePack) {
		setWidth(new RelativeDimensionConstraint(1));
		setHeight(new RelativeDimensionConstraint(1));
		setPosX(new PixelPositionConstraint(0));
		setPosY(new PixelPositionConstraint(0));
		ColourGui cardHolder = new ColourGui(resourcePack.defaultShaderProgram(), resourcePack.rectangleVAO(), Colour.colour(117, 96, 60));
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

}