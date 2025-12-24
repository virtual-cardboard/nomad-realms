package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class UnrevealedCardUI implements UI {

	private static final float STACK_OFFSET_PER_CARD = 2;
	private static final int UNREVEALED_CARD_COLOR = rgb(100, 100, 100);

	private final Deck deck;
	private final ConstraintBox constraintBox;

	public UnrevealedCardUI(Deck deck, ConstraintBox constraintBox) {
		this.deck = deck;
		this.constraintBox = constraintBox;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (deck.size() <= 1) {
			return;
		}
		DefaultFrameBuffer.instance().render(() -> {
			for (int i = 1; i < deck.size(); i++) {
				float offset = i * STACK_OFFSET_PER_CARD;
				ConstraintBox cardBox = new ConstraintBox(
						constraintBox.x().add(offset),
						constraintBox.y().add(offset),
						constraintBox.w(),
						constraintBox.h()
				);
				re.defaultShaderProgram
						.set("color", toRangedVector(UNREVEALED_CARD_COLOR))
						.set("transform", new Matrix4f(cardBox, re.glContext))
						.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
			}
		});
	}

}
