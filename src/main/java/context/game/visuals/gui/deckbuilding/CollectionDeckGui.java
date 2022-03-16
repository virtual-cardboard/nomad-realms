package context.game.visuals.gui.deckbuilding;

import static context.visuals.colour.Colour.rgb;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import context.GLContext;
import context.data.GameData;
import context.game.NomadsGameData;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.RelativePositionConstraint;

public class CollectionDeckGui extends Gui {

	private RectangleRenderer rectangleRenderer;
	private List<CollectionCardGui> cardGuis = new ArrayList<>();

	public CollectionDeckGui(RectangleRenderer rectangleRenderer, NomadsSettings s) {
		setWidth(new PixelDimensionConstraint(s.cardWidth() * 1.2f));
		setHeight(new RelativeDimensionConstraint(0.8f));
		setPosX(new RelativePositionConstraint(0.7f));
		setPosY(new RelativePositionConstraint(0.1f));
		this.rectangleRenderer = rectangleRenderer;
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		rectangleRenderer.render(x, y, width, height, rgb(249, 198, 48));
		NomadsGameData nomadsData = (NomadsGameData) data;
		NomadsSettings settings = nomadsData.settings();
		for (CollectionCardGui cardGui : cardGuis) {
			cardGui.render(glContext, settings);
		}
	}

}
