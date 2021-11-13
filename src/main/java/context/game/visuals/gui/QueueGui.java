package context.game.visuals.gui;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;
import model.card.CardDashboard;

public class QueueGui extends CardZoneGui {

	private CardDashboard cardDashboard;
	private TextureRenderer textureRenderer;
	private Texture queueTexture;

	public QueueGui(CardDashboard cardDashboard, ResourcePack resourcePack) {
		this.cardDashboard = cardDashboard;
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		queueTexture = resourcePack.getTexture("queue_gui");
		setWidth(new PixelDimensionConstraint(queueTexture.width() * 0.85f));
		setHeight(new PixelDimensionConstraint(queueTexture.height() * 0.85f));
		setPosX(new PixelPositionConstraint(0));
		setPosY(new PixelPositionConstraint(0));
	}

	@Override
	public void render(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
		if (cardDashboard.deck().empty()) {
			return;
		}
		matrix4f.translate(x, y).scale(width, height);
		textureRenderer.render(glContext, queueTexture, matrix4f);
	}

	@Override
	public void resetTargetPositions(Vector2f screenDimensions) {
		float increment = CardGui.HEIGHT * 0.35f;
		for (int i = 0; i < cardGuis.size(); i++) {
			cardGuis.get(i).setTargetPos(CardGui.WIDTH * 0.5f, CardGui.HEIGHT * 0.5f + increment * i - 60);
		}
	}

}
