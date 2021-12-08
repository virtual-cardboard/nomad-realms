package context.game.visuals.renderer;

import static common.math.Vector3f.Z_AXIS;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.visuals.colour.Colour;
import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.TextureRenderer;
import model.particle.Particle;

public class ParticleRenderer extends GameRenderer {

	private TextureRenderer textureRenderer;

	public ParticleRenderer(TextureRenderer textureRenderer) {
		this.textureRenderer = textureRenderer;
	}

	public void render(GLContext glContext, Vector2f screenDim, Particle p) {
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / screenDim.x, 1 / screenDim.y);
		matrix4f.translate(p.pos).translate(p.dim.scale(0.5f)).rotate(p.rot, Z_AXIS).translate(p.dim.scale(0.5f).negate()).scale(p.dim);
		textureRenderer.render(glContext, p.tex, matrix4f, Colour.rgba(255, 255, 255, (int) (p.opacity * 255)));
		p.update();
	}

}
