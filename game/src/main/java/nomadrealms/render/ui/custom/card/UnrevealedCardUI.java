package nomadrealms.render.ui.custom.card;

import engine.common.math.Matrix4f;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class UnrevealedCardUI implements UI {

	private final Deck deck;
	private final ConstraintBox constraintBox;

	public UnrevealedCardUI(Deck deck, ConstraintBox constraintBox) {
		this.deck = deck;
		this.constraintBox = constraintBox;
	}

	@Override
	public void render(RenderingEnvironment re) {
		for (int i = deck.size() - 2; i >= 0; i--) {
			re.textureRenderer.render(
					re.imageMap.get("card_back"),
					new Matrix4f(constraintBox.translate(0, i * 2), re.glContext)
			);
		}
	}

}
