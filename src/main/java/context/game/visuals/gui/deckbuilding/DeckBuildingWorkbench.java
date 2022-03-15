package context.game.visuals.gui.deckbuilding;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import common.math.PosDim;
import context.GLContext;
import context.ResourcePack;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.colour.Colour;
import context.visuals.gui.RootGui;
import context.visuals.gui.constraint.dimension.GuiDimensionConstraint;
import context.visuals.gui.constraint.position.GuiPositionConstraint;
import model.card.CollectionCard;
import model.state.GameState;

public class DeckBuildingWorkbench {

	private List<CollectionCard> deck = new ArrayList<>();
	private List<CollectionCard> collection = new ArrayList<>();

	private RootGui rootGui;
	private RectangleRenderer rectangleRenderer;

	private GuiPositionConstraint posX;
	private GuiPositionConstraint posY;
	private GuiDimensionConstraint width;
	private GuiDimensionConstraint height;

	public DeckBuildingWorkbench(ResourcePack rp, RootGui rootGui) {
		this.rootGui = rootGui;
		this.rectangleRenderer = rp.getRenderer("rectangle", RectangleRenderer.class);
	}

	public void render(GLContext glContext, NomadsSettings s, GameState state) {
		PosDim pd = posDim();
		rectangleRenderer.render(pd.x, pd.y, pd.w, pd.h, Colour.rgb(135, 117, 59));
	}

	public PosDim posDim() {
		float x = posX.calculateValue(0, rootGui.width());
		float y = posY.calculateValue(0, rootGui.height());
		float w = width.calculateValue(0, rootGui.width());
		float h = height.calculateValue(0, rootGui.height());
		return new PosDim(x, y, w, h);
	}

}
