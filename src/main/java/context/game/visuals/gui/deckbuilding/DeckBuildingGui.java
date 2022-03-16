package context.game.visuals.gui.deckbuilding;

import static context.visuals.colour.Colour.rgb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.game.visuals.gui.CardGui;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.RelativePositionConstraint;
import model.card.CollectionCard;
import model.id.WorldCardID;

public class DeckBuildingGui extends Gui {

	private Map<WorldCardID, CardGui> cardGuis = new HashMap<>();

	private List<CollectionCard> deck = new ArrayList<>();
	private List<CollectionCard> collection = new ArrayList<>();

	private RectangleRenderer rectangleRenderer;

	public DeckBuildingGui(ResourcePack rp) {
		this.rectangleRenderer = rp.getRenderer("rectangle", RectangleRenderer.class);
		setWidth(new RelativeDimensionConstraint(0.8f));
		setHeight(new RelativeDimensionConstraint(0.8f));
		setPosX(new RelativePositionConstraint(0.1f));
		setPosY(new RelativePositionConstraint(0.1f));
		CardCollectionGui cardCollectionGui = new CardCollectionGui(rectangleRenderer);
		addChild(cardCollectionGui);
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		rectangleRenderer.render(x, y, width, height, rgb(135, 117, 59));
	}

}
