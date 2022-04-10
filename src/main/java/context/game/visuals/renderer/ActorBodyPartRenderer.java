package context.game.visuals.renderer;

import static java.lang.Double.compare;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.List;

import app.NomadsSettings;
import context.GLContext;
import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.LineRenderer;
import context.visuals.renderer.TextureRenderer;
import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import graphics.displayable.ActorBodyPart;
import graphics.displayable.LimbBodyPart;
import graphics.displayable.TextureBodyPart;

public class ActorBodyPartRenderer extends GameRenderer {

	private TextureRenderer textureRenderer;
	private LineRenderer lineRenderer;

	public ActorBodyPartRenderer(TextureRenderer textureRenderer, LineRenderer lineRenderer) {
		this.textureRenderer = textureRenderer;
		this.lineRenderer = lineRenderer;
	}

	public void render(NomadsSettings s, List<ActorBodyPart> parts, Vector2f screenPos, Vector2f direction) {
		parts.sort((c1, c2) -> compare(c1.dist * sin(c1.rot + direction.angle()), c2.dist * sin(c2.rot + direction.angle())));
		for (int i = 0; i < parts.size(); i++) {
			parts.get(i).render(this, glContext, s, screenPos, direction);
		}
	}

	public void renderTextureBodyPart(GLContext glContext, NomadsSettings s, TextureBodyPart part, Vector2f screenPos, Vector2f direction) {
		float xScale = (float) cos(part.rot + direction.angle());
		float x = part.dist * xScale;

		Vector2i windowDim = glContext.windowDim();
		Matrix4f m = new Matrix4f().translate(-1, 1).scale(2f / windowDim.x, -2f / windowDim.y);
		m.translate(x * part.texScale, -part.height * part.texScale).translate(screenPos);

		float scale = 1 - abs(xScale) * (1 - part.minScale);
		m.scale(scale, 1);
		m.translate(part.tex.dimensions().scale(0.5f * part.texScale).negate());
		m.scale(part.tex.dimensions().scale(part.texScale));
		textureRenderer.render(part.tex, m);
	}

	public void renderLimbBodyPart(GLContext glContext, LimbBodyPart p, NomadsSettings s, Vector2f screenPos, Vector2f direction) {
		float xScale = (float) cos(p.rot + direction.angle());
		Vector2f p1 = new Vector2f(p.dist, -p.height);
		Vector2f p2;
		Vector2f p3 = new Vector2f(p.pointDist, -p.pointHeight);

		Vector2f p1ToP3 = p3.sub(p1);
		float armLength = p.armLength;
		float foreArmLength = p.foreArmLength;
		if (p1ToP3.lengthSquared() > (armLength + foreArmLength) * (armLength + foreArmLength)) {
			p1ToP3 = p1ToP3.normalise().scale(armLength + foreArmLength);
			p3 = p1.add(p1ToP3);
			p2 = p1.add(p1ToP3.scale(0.5f));
		} else {
			double angleC = Math
					.acos((p1ToP3.lengthSquared() + armLength * armLength - foreArmLength * foreArmLength)
							/ (2 * p1ToP3.length() * armLength));
			double theta = Math.atan2(p1ToP3.y, p1ToP3.x);
			float angle = (float) (p.clockwise ? (theta + angleC) : (theta - angleC));
			p2 = Vector2f.fromAngleLength(angle, armLength).add(p1);
		}

		Vector2f f1 = screenPos.add(p1.multiply(xScale, 1));
		Vector2f f2 = screenPos.add(p2.multiply(xScale, 1));
		Vector2f f3 = screenPos.add(p3.multiply(xScale, 1));

		lineRenderer.render(f1.x, f1.y, f2.x, f2.y, p.armWidth, p.colour);
		lineRenderer.render(f2.x, f2.y, f3.x, f3.y, p.foreArmWidth, p.colour);
	}

}
