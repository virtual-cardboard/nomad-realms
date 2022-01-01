package context.game.visuals.gui;

import static context.game.visuals.gui.CardGui.HEIGHT;
import static context.game.visuals.gui.CardGui.WIDTH;

import java.util.List;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;
import model.card.CardDashboard;

public class DeckGui extends CardZoneGui {

	private CardDashboard cardDashboard;
	private TextureRenderer textureRenderer;
	private Texture base;
	private Texture cardBackWood;
	private Texture logo;

	public DeckGui(CardDashboard cardDashboard, ResourcePack resourcePack) {
		this.cardDashboard = cardDashboard;
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		base = resourcePack.getTexture("card_base");
		cardBackWood = resourcePack.getTexture("card_back_wood");
		logo = resourcePack.getTexture("logo_small");
		setWidth(new PixelDimensionConstraint(WIDTH));
		setHeight(new PixelDimensionConstraint(HEIGHT));
		setPosX(new PixelPositionConstraint(-10, width()));
		setPosY(new PixelPositionConstraint(-CardGui.HEIGHT * 0.15f, height()));
	}

	@Override
	public void render(GLContext glContext, Vector2f screenDim, float x, float y, float w, float h) {
		List<CardGui> cardGuis = cardGuis();
		for (int i = cardGuis.size() - 1; i >= 0; i--) {
			if (cardGuis.get(i).inPlace()) {
				removeCardGui(i);
			}
		}
		if (cardDashboard.deck().empty()) {
			return;
		}
		Matrix4f matrix4f = rectToPixelMatrix4f(screenDim).translate(x, y).scale(w, h);
		textureRenderer.render(glContext, base, matrix4f);
		textureRenderer.render(glContext, cardBackWood, matrix4f);
		textureRenderer.render(glContext, screenDim, logo, x + w * 0.5f, y + h * 0.5f, 0.4f);
	}

}
