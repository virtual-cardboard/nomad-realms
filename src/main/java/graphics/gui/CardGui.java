package graphics.gui;

import static context.visuals.colour.Colour.colour;

import common.math.Matrix4f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import model.card.GameCard;

public class CardGui extends Gui {

	private GameCard card;
	private TextureRenderer textureRenderer;
	private TextRenderer textRenderer;
	private Texture base;
	private Texture decoration;
	private Texture front;
	private Texture banner;
	private GameFont font;

	public CardGui(GameCard card, TextureRenderer textureRenderer, TextRenderer textRenderer, ResourcePack resourcePack) {
		this.card = card;
		this.textureRenderer = textureRenderer;
		this.textRenderer = textRenderer;
		base = resourcePack.getTexture("card_base");
		decoration = resourcePack.getTexture("card_decoration_" + card.type().name);
		front = resourcePack.getTexture("card_front");
		banner = resourcePack.getTexture("card_banner");
		font = resourcePack.getFont("baloo2");
		setWidth(new PixelDimensionConstraint(256));
		setHeight(new PixelDimensionConstraint(512));

	}

	@Override
	public void render(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
		Matrix4f clone = matrix4f.clone().translate(x, y).scale(width, height);
		textureRenderer.render(glContext, base, clone);
		textureRenderer.render(glContext, decoration, clone);
		textureRenderer.render(glContext, front, clone);
		textureRenderer.render(glContext, banner, clone);
		textureRenderer.render(glContext, card.texture(), clone);
		textRenderer.render(glContext, matrix4f.clone(), card.name(), x + width * 0.3f, y + height * 0.45f, width, font, width * 0.07f, colour(28, 68, 124));
		textRenderer.render(glContext, matrix4f, card.text(), x + width * 0.21f, y + height * 0.52f, width * 0.58f, font, width * 0.06f, colour(28, 68, 124));
	}

}
