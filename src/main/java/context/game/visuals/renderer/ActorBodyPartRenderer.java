package context.game.visuals.renderer;

import java.util.List;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.displayable.ActorBodyPart;
import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.TextureRenderer;

public class ActorBodyPartRenderer extends GameRenderer {

	private TextureRenderer textureRenderer;

	public ActorBodyPartRenderer(TextureRenderer textureRenderer) {
		this.textureRenderer = textureRenderer;
	}

	public void render(GLContext glContext, Vector2f screenDim, List<ActorBodyPart> parts, Vector2f position, Vector2f direction) {
		parts.sort((c1, c2) -> Double.compare(c1.dist * Math.sin(c1.rot) * direction.y, c2.dist * Math.sin(c2.rot) * direction.y));
		for (int i = 0; i < parts.size(); i++) {
			ActorBodyPart part = parts.get(i);
			float cos = (float) Math.cos(part.rot + Math.PI / 2) + direction.x;
			float x = part.dist * cos;
			Matrix4f m = new Matrix4f().translate(-1, 1).scale(2f / screenDim.x, -2f / screenDim.y);
			m.translate(x * part.texScale, -part.height * part.texScale).translate(position);

			float scale = 1 - Math.abs(cos) * (1 - part.minScale);
			m.scale(scale, 1);
			m.translate(part.tex.dimensions().scale(0.5f * part.texScale).negate());
			m.scale(part.tex.dimensions().scale(part.texScale));
			textureRenderer.render(glContext, part.tex, m);
		}
	}

}
