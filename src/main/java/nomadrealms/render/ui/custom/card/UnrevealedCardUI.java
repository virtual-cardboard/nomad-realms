package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgba;

import engine.common.math.Matrix4f;
import engine.context.input.Mouse;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class UnrevealedCardUI implements UI {

	private final Deck deck;
	private final ConstraintBox constraintBox;
	private final Mouse mouse;

	public UnrevealedCardUI(Deck deck, ConstraintBox constraintBox, Mouse mouse) {
		this.deck = deck;
		this.constraintBox = constraintBox;
		this.mouse = mouse;
	}

	@Override
	public void render(RenderingEnvironment re) {
		float alpha;
		if (constraintBox.contains(mouse.coordinate())) {
			alpha = 0.8f;
		} else {
			alpha = 0.4f;
		}
		for (int i = deck.size() - 2; i >= 0; i--) {
			re.textureRenderer.setDiffuse(rgba(255, 255, 255, (int) (alpha * 255)));
			re.textureRenderer.render(
					re.imageMap.get("card_back"),
					new Matrix4f(constraintBox.translate(0, i * 2), re.glContext)
			);
			re.textureRenderer.resetDiffuse();
		}
	}

}
