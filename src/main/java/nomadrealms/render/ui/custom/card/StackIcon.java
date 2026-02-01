package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.render.ui.custom.card.TargetingRenderer.renderTargetingArrow;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import engine.visuals.rendering.texture.ImageCropBox;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class StackIcon implements UI {

	private final CardPlayedEvent event;
	private final ConstraintBox constraintBox;

	public StackIcon(CardPlayedEvent event, ConstraintBox constraintBox) {
		this.event = event;
		this.constraintBox = constraintBox;
	}

	@Override
	public void render(RenderingEnvironment re) {
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(210, 180, 140)))
				.set("transform", new Matrix4f(constraintBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		re.textureRenderer.render(
				re.imageMap.get(event.card().card().artwork()),
				new Matrix4f(constraintBox, re.glContext),
				new ImageCropBox(new ConstraintBox(absolute(0.1f), absolute(0.1f), absolute(0.8f), absolute(0.8f)))
		);

		if (constraintBox.contains(re.mouse.coordinate()) && event.target() != null) {
			Target target = event.target();
			Vector2f targetPos = target.tile().getScreenPosition(re).vector();
			Vector2f startPos = constraintBox.center().vector();
			renderTargetingArrow(re, startPos, targetPos, targetPos);
		}
	}

}
