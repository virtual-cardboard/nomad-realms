package nomadrealms.render.particle.context.game;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import static java.lang.Math.PI;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import java.util.function.Function;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.geometry.RectangleParticle;

public class CardParticle extends RectangleParticle {

	public Function<RenderingEnvironment, ConstraintBox> box;

	public CardParticle(CardPlayedEvent event) {
		super(1000, null, time().multiply(2 * PI / 1000), rgb(155, 130, 95));
		this.box = (RenderingEnvironment re) ->
				new ConstraintBox(
						event.source().getScreenPosition(re).add(
								new ConstraintPair(
										absolute(0),
										time().multiply(time()).multiply(0.0001f).sub(time().multiply(0.1f))
								).scale(re.camera.zoom())
						),
						new ConstraintPair(absolute(10), absolute(14)).scale(re.camera.zoom())
				);
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (box() == null) {
			box(box.apply(re));
		}
		float outlineSize = 1 * re.camera.zoom().get();
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(69, 50, 36)))
				.set("transform", new Matrix4f()
						.translate(-1, 1)
						.scale(2, -2)
						.scale(1 / re.glContext.width(), 1 / re.glContext.height())
						.translate(box().coordinate().vector())
						.translate(0.5f, 0.5f, 0)
						.rotate(rotation().get(), new Vector3f(0, 0, 1))
						.translate(-0.5f, -0.5f, 0)
						.scale(box().w().get() + 2 * outlineSize, box().h().get() + 2 * outlineSize)
						.translate(-0.5f, -0.5f, 0))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		super.render(re);
	}
}
