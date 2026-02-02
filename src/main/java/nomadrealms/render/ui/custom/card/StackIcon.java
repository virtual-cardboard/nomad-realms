package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import engine.visuals.rendering.texture.ImageCropBox;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.zone.CardStackEntry;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class StackIcon implements UI {

	private final CardStackEntry entry;
	private final ConstraintBox constraintBox;

	public StackIcon(CardStackEntry entry, ConstraintBox constraintBox) {
		this.entry = entry;
		this.constraintBox = constraintBox;
	}

	@Override
	public void render(RenderingEnvironment re) {
		Constraint padding = absolute(2).multiply(re.camera.zoom());
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(210, 180, 140)))
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
				Target target = entry.event().target();
				new Arrow(uiCard.centerPosition(), target.tile().getScreenPosition(re)).targetCenter(target.tile().getScreenPosition(re)).render(re);
			}
			uiCard.render(re);
		}
	}

}
