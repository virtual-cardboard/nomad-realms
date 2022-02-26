package context.game.visuals.gui;

import java.util.List;

import app.NomadsSettings;
import common.math.Matrix4f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;
import model.state.GameState;

public class DeckGui extends CardZoneGui {

	private TextureRenderer textureRenderer;
	private Texture base;
	private Texture cardBackWood;
	private Texture logo;

	public DeckGui(ResourcePack resourcePack, NomadsSettings settings) {
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		base = resourcePack.getTexture("card_base");
		cardBackWood = resourcePack.getTexture("card_back_wood");
		logo = resourcePack.getTexture("logo_small");
		setWidth(new PixelDimensionConstraint(settings.cardWidth()));
		setHeight(new PixelDimensionConstraint(settings.cardHeight()));
		setPosX(new PixelPositionConstraint(20, width()));
		setPosY(new PixelPositionConstraint(20, height()));
	}

	@Override
	public void render(GLContext glContext, NomadsSettings s, GameState state, float x, float y, float w, float h) {
		List<CardGui> cardGuis = cardGuis();
		for (int i = cardGuis.size() - 1; i >= 0; i--) {
			if (cardGuis.get(i).inPlace()) {
				removeCardGui(i);
			}
		}
		if (playerID().getFrom(state).cardDashboard().deck().empty()) {
			return;
		}
		Matrix4f matrix4f = rectToPixelMatrix4f(glContext.windowDim()).translate(x, y).scale(w, h);
		textureRenderer.render(base, matrix4f);
		textureRenderer.render(cardBackWood, matrix4f);
		textureRenderer.render(logo, x + w * 0.5f, y + h * 0.5f, 0.4f);
	}

}
