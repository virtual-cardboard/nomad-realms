package context.game.visuals.gui;

import static context.game.visuals.gui.CardGui.HEIGHT;
import static context.game.visuals.gui.CardGui.WIDTH;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.CardType.CREATURE;
import static model.card.CardType.STRUCTURE;

import java.util.HashMap;
import java.util.Map;

import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.Texture;
import model.card.CardDashboard;
import model.card.CardType;

public class DiscardGui extends CardZoneGui {

//	private CardDashboard cardDashboard;
//	private TextureRenderer textureRenderer;
//	private TextRenderer textRenderer;
//	private Texture base;
	private Map<CardType, Texture> decorations = new HashMap<>(4, 1);
//	private Texture front;
//	private Texture banner;
//	private GameFont font;

	public DiscardGui(CardDashboard cardDashboard, ResourcePack resourcePack) {
//		this.cardDashboard = cardDashboard;
//		this.textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
//		this.textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
//		base = resourcePack.getTexture("card_base");
		decorations.put(ACTION, resourcePack.getTexture("card_decoration_action"));
		decorations.put(CANTRIP, resourcePack.getTexture("card_decoration_cantrip"));
		decorations.put(CREATURE, resourcePack.getTexture("card_decoration_creature"));
		decorations.put(STRUCTURE, resourcePack.getTexture("card_decoration_structure"));
//		front = resourcePack.getTexture("card_front");
//		banner = resourcePack.getTexture("card_banner");
//		font = resourcePack.getFont("baloo2");
		setPosX(new PixelPositionConstraint(0));
		setPosY(new BiFunctionPositionConstraint((start, end) -> end - HEIGHT));
		setWidth(new PixelDimensionConstraint(WIDTH));
		setHeight(new PixelDimensionConstraint(HEIGHT));
	}

	@Override
	public void render(GLContext glContext, Vector2f vector2f, float x, float y, float width, float height) {
		boolean cardGuiOnTop = false;
		for (int i = cardGuis.size() - 2; i >= 0; i--) {
			if (cardGuiOnTop) {
				removeCardGui(i);
			} else if (cardGuis.get(i).inPlace()) {
				cardGuiOnTop = true;
			}
		}
	}

	@Override
	public void resetTargetPositions(Vector2f screenDimensions) {
		super.resetTargetPositions(screenDimensions);
	}

}
