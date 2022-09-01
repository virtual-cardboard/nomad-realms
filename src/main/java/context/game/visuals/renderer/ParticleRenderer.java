package context.game.visuals.renderer;

import static context.visuals.colour.Colour.a;
import static context.visuals.colour.Colour.b;
import static context.visuals.colour.Colour.g;
import static context.visuals.colour.Colour.r;
import static context.visuals.colour.Colour.rgba;
import static engine.common.math.Vector2f.fromAngleLength;
import static engine.common.math.Vector3f.Z_AXIS;

import java.util.ArrayList;
import java.util.List;

import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.LineRenderer;
import context.visuals.renderer.TextureRenderer;
import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import graphics.particle.LineParticle;
import graphics.particle.Particle;
import graphics.particle.TextureParticle;

public class ParticleRenderer extends GameRenderer {

	private final List<Particle> particles = new ArrayList<>();
	private final TextureRenderer textureRenderer;
	private final LineRenderer lineRenderer;

	public ParticleRenderer(TextureRenderer textureRenderer, LineRenderer lineRenderer) {
		this.textureRenderer = textureRenderer;
		this.lineRenderer = lineRenderer;
	}

	public void renderParticles() {
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle p = particles.get(i);
			if (p.isDead()) {
				p.cleanup();
				particles.remove(i);
				continue;
			}
			renderParticle(p);
		}
	}

	private void renderParticle(Particle p) {
		if (p.delay == 0) {
			p.render(this);
		}
		p.update();
	}

	public void render(TextureParticle p) {
		float x = p.xFunc.apply(p.age);
		float y = p.yFunc.apply(p.age);
		float rot = p.rotFunc.apply(p.age);
		int diffuse = p.colourFunc.apply(p.age);

		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / glContext.width(), 1 / glContext.height());
		matrix4f.translate(x, y).translate(p.dim.scale(0.5f)).rotate(rot, Z_AXIS).translate(p.dim.scale(0.5f).negate()).scale(p.dim);
		textureRenderer.setDiffuse(diffuse);
		textureRenderer.render(p.tex, matrix4f);
		textureRenderer.resetDiffuse();
	}

	public void render(LineParticle p) {
		float x = p.xFunc.apply(p.age);
		float y = p.yFunc.apply(p.age);
		float rot = p.rotFunc.apply(p.age);
		int diffuse = p.colourFunc.apply(p.age);

		Vector2f addOffset = fromAngleLength(rot, p.length).add(x, y);
		int colour = rgba(r(diffuse), g(diffuse), b(diffuse), a(diffuse));
		lineRenderer.render(x, y, addOffset.x(), addOffset.y(), p.width, colour);
	}

	public List<Particle> particles() {
		return particles;
	}

}
