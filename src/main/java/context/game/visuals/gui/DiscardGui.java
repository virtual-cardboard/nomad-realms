package context.game.visuals.gui;

import static context.game.visuals.gui.CardGui.HEIGHT;
import static context.game.visuals.gui.CardGui.WIDTH;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.CardType.CREATURE;
import static model.card.CardType.STRUCTURE;

import java.util.HashMap;
import java.util.Map;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import model.card.CardDashboard;
import model.card.CardType;

public class DiscardGui extends CardZoneGui {

	private CardDashboard cardDashboard;
	private TextureRenderer textureRenderer;
	private TextRenderer textRenderer;
	private Texture base;
	private Map<CardType, Texture> decorations = new HashMap<>(4, 1);
	private Texture front;
	private Texture banner;
	private GameFont font;

	public DiscardGui(CardDashboard cardDashboard, ResourcePack resourcePack) {
		this.cardDashboard = cardDashboard;
		this.textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		this.textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		base = resourcePack.getTexture("card_base");
		decorations.put(ACTION, resourcePack.getTexture("card_decoration_action"));
		decorations.put(CANTRIP, resourcePack.getTexture("card_decoration_cantrip"));
		decorations.put(CREATURE, resourcePack.getTexture("card_decoration_creature"));
		decorations.put(STRUCTURE, resourcePack.getTexture("card_decoration_structure"));
		front = resourcePack.getTexture("card_front");
		banner = resourcePack.getTexture("card_banner");
		font = resourcePack.getFont("baloo2");
		setPosX(new PixelPositionConstraint(0));
		setPosY(new BiFunctionPositionConstraint((start, end) -> end - HEIGHT));
		setWidth(new PixelDimensionConstraint(WIDTH));
		setHeight(new PixelDimensionConstraint(HEIGHT));
	}

	@Override
	public void render(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
//		if (cardDashboard.discard().size() == 0) {
//			return;
//		}
//		GameCard card = cardDashboard.discard().card(0);
//		Texture decoration = decorations.get(card.type());
//		Matrix4f clone = matrix4f.copy().translate(x, y).scale(width, height);
//		textureRenderer.render(glContext, base, clone);
//		textureRenderer.render(glContext, decoration, clone);
//		textureRenderer.render(glContext, front, clone);
//		textureRenderer.render(glContext, banner, clone);
//		textureRenderer.render(glContext, card.texture(), clone);
//		textRenderer.render(glContext, matrix4f.copy(), card.name(), x + width * 0.3f, y + height * 0.45f, width, font, width * 0.07f, rgb(28, 68, 124));
//		textRenderer.render(glContext, matrix4f, card.text(), x + width * 0.21f, y + height * 0.52f, width * 0.58f, font, width * 0.06f, rgb(28, 68, 124));
	}

	@Override
	public void resetTargetPositions(Vector2f screenDimensions) {
		super.resetTargetPositions(screenDimensions);
	}

}
