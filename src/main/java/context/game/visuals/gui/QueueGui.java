package context.game.visuals.gui;

import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;

public class QueueGui extends CardZoneGui {

	private TextureRenderer textureRenderer;
	private Texture queueTexture;

	public QueueGui(ResourcePack resourcePack) {
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		queueTexture = resourcePack.getTexture("queue_gui");
		setWidth(new PixelDimensionConstraint(queueTexture.width() * 0.85f));
		setHeight(new PixelDimensionConstraint(queueTexture.height() * 0.85f));
		setPosX(new PixelPositionConstraint(0));
		setPosY(new PixelPositionConstraint(0));
	}

	@Override
	public void render(GLContext glContext, Vector2f screenDim, float x, float y, float width, float height) {
		textureRenderer.render(glContext, queueTexture, rectToPixelMatrix4f(screenDim).translate(x, y).scale(width, height));
	}

	@Override
	public void resetTargetPositions(Vector2f screenDimensions) {
		float increment = CardGui.HEIGHT * 0.15f;
		for (int i = 0; i < cardGuis.size(); i++) {
			cardGuis.get(i).setTargetPos(CardGui.WIDTH * 0.5f, CardGui.HEIGHT * 0.5f + increment * i - 60);
		}
	}

}
