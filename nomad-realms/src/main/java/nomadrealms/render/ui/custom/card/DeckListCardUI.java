package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import static engine.visuals.rendering.text.HorizontalAlign.LEFT;
import static engine.visuals.rendering.text.HorizontalAlign.RIGHT;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.BasicUIContent;

public class DeckListCardUI extends BasicUIContent {

	private final GameCard card;

	public DeckListCardUI(GameCard card, ConstraintBox box) {
		super(box);
		this.card = card;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		// Render background strip
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(80, 80, 80)))
				.set("transform", new Matrix4f(constraintBox(), re.glContext))
				.use(new DrawFunction()
						.vao(RectangleVertexArrayObject.instance())
						.glContext(re.glContext));

		// Mana Cost (Left)
		re.textRenderer.render(
				textFormat()
						.text(String.valueOf(card.manaCost()))
						.font(re.font)
						.fontSize(14)
						.colour(rgb(255, 255, 255))
						.hAlign(LEFT)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel().copy().translate(
								constraintBox().x().get() + 8,
								constraintBox().y().get() + 4))
		);

		// Title (Middle/Left aligned after cost)
		re.textRenderer.render(
				textFormat()
						.text(card.title())
						.font(re.font)
						.fontSize(14)
						.colour(rgb(255, 255, 255))
						.hAlign(LEFT)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel().copy().translate(
								constraintBox().x().get() + 35,
								constraintBox().y().get() + 4))
		);

		// Resolution Time (Right)
		re.textRenderer.render(
				textFormat()
						.text(String.valueOf(card.resolutionTime()))
						.font(re.font)
						.fontSize(14)
						.colour(rgb(255, 255, 255))
						.hAlign(RIGHT)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel().copy().translate(
								constraintBox().x().get() + constraintBox().w().get() - 10,
								constraintBox().y().get() + 4))
		);
	}

}
