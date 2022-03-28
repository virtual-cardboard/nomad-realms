package context.game.visuals.gui.deckbuilding;

import static context.visuals.colour.Colour.rgb;

import java.util.List;

import app.NomadsSettings;
import common.math.PosDim;
import context.GLContext;
import context.data.GameData;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.RelativePositionConstraint;

public class CollectionDeckGui extends Gui {

	private RectangleRenderer rectangleRenderer;

	public CollectionDeckGui(RectangleRenderer rectangleRenderer, NomadsSettings s) {
		setWidth(new PixelDimensionConstraint(s.cardWidth() * 1.2f));
		setHeight(new RelativeDimensionConstraint(0.8f));
		setPosX(new RelativePositionConstraint(0.75f));
		setPosY(new RelativePositionConstraint(0.1f));
		this.rectangleRenderer = rectangleRenderer;
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		rectangleRenderer.render(x, y, width, height, rgb(249, 198, 48));
	}

	public void resetTargetPositions(NomadsSettings settings) {
		PosDim pd = posdim();

		List<Gui> children = getChildren();
		float height = settings.cardHeight();
		for (int i = 0; i < children.size(); i++) {
			Gui gui = children.get(i);
			CollectionCardGui cardGui = (CollectionCardGui) gui;
			cardGui.setTargetPos(pd.x + pd.w / 2, pd.y + height / 2 + i * height * 0.15f);
		}
	}

}
