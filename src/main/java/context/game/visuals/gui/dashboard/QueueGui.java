package context.game.visuals.gui.dashboard;

import java.util.List;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;
import model.state.GameState;

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
	public void doRender(GLContext glContext, NomadsSettings s, GameState state, float x, float y, float width, float height) {
		textureRenderer.render(queueTexture, rectToPixelMatrix4f(glContext.windowDim()).translate(x, y).scale(width, height));
	}

	@Override
	public void resetTargetPositions(Vector2f screenDimensions, NomadsSettings settings) {
		float increment = settings.cardHeight() * 0.15f;
		List<WorldCardGui> cardGuis = cardGuis();
		for (int i = 0; i < cardGuis.size(); i++) {
			cardGuis.get(i).setTargetPos(settings.cardDim().scale(0.5f).add(20, increment * i + 20));
		}
	}

}
