package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.constraint.posdim.SineConstraint.sin;

import engine.common.math.Matrix4f;
import engine.common.math.Vector4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.posdim.CustomSupplierConstraint;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import engine.visuals.rendering.texture.ImageCropBox;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.zone.CardStackEntry;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class StackIcon implements UI {

	private final CardStackEntry entry;
	private ConstraintBox constraintBox;

	private long startFrame = -1;
	private boolean wasTargeting = false;
	private final Constraint pulse;

	public StackIcon(CardStackEntry entry) {
		this.entry = entry;
		this.pulse = sin(new CustomSupplierConstraint("frameNumber", () -> (float) (entry.counter() - startFrame)).multiply(0.15f))
				.add(1).multiply(0.5f);
	}

	public StackIcon constraintBox(ConstraintBox constraintBox) {
		this.constraintBox = constraintBox;
		return this;
	}

	@Override
	public void render(RenderingEnvironment re) {
		Constraint padding = absolute(2).multiply(re.camera.zoom());
		int tan = rgb(210, 180, 140);
		Vector4f tanVec = toRangedVector(tan);
		Vector4f redVec = toRangedVector(rgb(255, 0, 0));
		Vector4f color;
		CardPlayer localPlayer = re.localPlayer != null ? re.localPlayer.cardPlayer() : null;
		Target target = entry.event().target();
		boolean isTargeting = localPlayer != null && (target == localPlayer || (target != null && target.tile() == localPlayer.tile()));
		if (isTargeting) {
			if (!wasTargeting) {
				startFrame = entry.counter();
			}
			float p = pulse.get();
			color = tanVec.scale(1 - p).add(redVec.scale(p));
		} else {
			color = tanVec;
		}
		wasTargeting = isTargeting;
		re.defaultShaderProgram
				.set("color", color)
				.set("transform", new Matrix4f(constraintBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		re.textureRenderer.render(
				re.imageMap.get(entry.event().card().card().artwork()),
				new Matrix4f(constraintBox, re.glContext),
				new ImageCropBox(new ConstraintBox(absolute(0.1f), absolute(0.1f), absolute(0.8f), absolute(0.8f)))
		);

		if (constraintBox.contains(re.mouse.coordinate())) {
			ConstraintBox cardBox = new ConstraintBox(
					constraintBox.x().add(constraintBox.w()).add(padding),
					constraintBox.y().add(constraintBox.h().multiply(0.5f)).add(UICard.cardSize(1.5f).y().multiply(-0.5f)),
					UICard.cardSize(1.5f)
			);
			UICard uiCard = new UICard(entry.event().card(), cardBox);
			if (entry.event().target() != null) {
				new Arrow(uiCard.centerPosition(), entry.event().target().tile().getScreenPosition(re))
						.targetCenter(entry.event().target().tile().getScreenPosition(re)).render(re);
			}
			uiCard.render(re);
		}
	}

}
