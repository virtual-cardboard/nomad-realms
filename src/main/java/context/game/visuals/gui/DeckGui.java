package context.game.visuals.gui;

import static context.game.visuals.gui.CardGui.HEIGHT;
import static context.game.visuals.gui.CardGui.WIDTH;

import common.math.Matrix4f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;
import model.card.CardDashboard;

public class DeckGui extends Gui {

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
		logo = resourcePack.getTexture("card_logo");
		setPosX(new BiFunctionPositionConstraint((start, end) -> end - WIDTH));
		setPosY(new BiFunctionPositionConstraint((start, end) -> end - HEIGHT));
		setWidth(new PixelDimensionConstraint(WIDTH));
		setHeight(new PixelDimensionConstraint(HEIGHT));
	}

	@Override
	public void render(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
		if (cardDashboard.deck().empty()) {
			return;
		}
		matrix4f.translate(x, y).scale(width, height);
		textureRenderer.render(glContext, base, matrix4f);
		textureRenderer.render(glContext, cardBackWood, matrix4f);
		textureRenderer.render(glContext, logo, matrix4f);
	}

}
