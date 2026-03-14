package nomadrealms.render.particle;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.rendering.text.HorizontalAlign;
import engine.visuals.rendering.text.TextFormat;
import engine.visuals.rendering.text.VerticalAlign;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.render.RenderingEnvironment;

import static engine.visuals.rendering.text.TextFormat.textFormat;

/**
 * A particle that renders a block of text.
 *
 * @author Lunkle
 */
public class TextParticle extends Particle {

	private String text;
	private int color;

	public TextParticle(long lifetime, ConstraintBox box, Constraint rotation, String text, int color) {
		super(lifetime, box, rotation);
		this.text = text;
		this.color = color;
	}

	public TextParticle(String text, int color) {
		this(1000, new ConstraintBox(absolute(0), absolute(0), absolute(10), absolute(10)), absolute(0), text, color);
	}

	@Override
	public void render(RenderingEnvironment re) {
		float x = box().x().get();
		float y = box().y().get();
		float fontSize = box().h().get();
		re.textRenderer.render(new Matrix4f()
						.translate(-1, 1)
						.scale(2, -2)
						.scale(1 / re.glContext.width(), 1 / re.glContext.height())
						.translate(x, y)
						.rotate(rotation().get(), new Vector3f(0, 0, 1)),
				textFormat()
						.text(text)
						.font(re.font)
						.fontSize(fontSize)
						.colour(color)
						.hAlign(HorizontalAlign.CENTER)
						.vAlign(VerticalAlign.CENTER));
	}

	@Override
	public Particle clone() {
		return new TextParticle(lifetime(), box(), rotation(), text, color);
	}

	public void text(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}

	public void color(int color) {
		this.color = color;
	}

	public int color() {
		return color;
	}

}
