package nomadrealms.render.ui.custom.indicator;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.CustomSupplierConstraint.custom;
import static engine.visuals.rendering.text.HorizontalAlign.LEFT;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.rendering.text.HorizontalAlign;
import engine.visuals.rendering.text.VerticalAlign;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class ManaIndicator implements UI {

	private static final long MANA_ERROR_ANIMATION_DURATION_MS = 500;
	private static final int MANA_ERROR_SHAKE_AMPLITUDE = 5;
	private static final int MANA_ERROR_SHAKE_FREQUENCY = 20;
	private static final int MANA_TEXT_X_OFFSET = 20;
	private static final int MANA_TEXT_Y_OFFSET = 0;
	private static final int MANA_BAR_WIDTH = 12;

	private final CardPlayer owner;
	private final ConstraintBox deckArea;
	private long lastManaErrorTime;

	public ManaIndicator(CardPlayer owner, ConstraintBox deckArea) {
		this.owner = owner;
		this.deckArea = deckArea;
	}

	public void triggerError() {
		this.lastManaErrorTime = System.currentTimeMillis();
	}

	@Override
	public void render(RenderingEnvironment re) {
		long timeSinceError = System.currentTimeMillis() - lastManaErrorTime;
		int color = (timeSinceError < MANA_ERROR_ANIMATION_DURATION_MS) ? rgb(255, 0, 0) : rgb(0, 0, 0);
		Constraint xPos = deckArea.x().add(MANA_TEXT_X_OFFSET).add(custom("shake", () -> {
			long t = System.currentTimeMillis() - lastManaErrorTime;
			return (t < MANA_ERROR_ANIMATION_DURATION_MS) ? (float) Math.sin(t / 1000.0 * MANA_ERROR_SHAKE_FREQUENCY * 2 * Math.PI) * MANA_ERROR_SHAKE_AMPLITUDE : 0;
		}));

		// Mana bar starts at 0.6x screen width (x = constraintBox.x().get())
		// and extends from middle center of the screen (y = 0.5 * h) to top center of the screen (y = 0).
		float barX = deckArea.x().get();
		float barY = deckArea.y().get(); // top center y (0)
		float barWidth = MANA_BAR_WIDTH;
		float totalBarHeight = deckArea.h().get();

		// Draw background vertical bar
		re.rectangleRenderer.render(barX, barY, barWidth, totalBarHeight, 0, rgb(50, 50, 50), rgb(30, 30, 30), 1);

		// Draw filled mana bar extending from bottom (middle center) towards top (top center)
		if (owner.maxMana() > 0) {
			float manaRatio = Math.max(0f, Math.min(1f, (float) owner.mana() / owner.maxMana()));
			float fillHeight = totalBarHeight * manaRatio;
			if (fillHeight > 0) {
				float fillY = barY + (totalBarHeight - fillHeight);
				re.rectangleRenderer.render(barX, fillY, barWidth, fillHeight, 0, rgb(0, 150, 255));
			}
		}

		re.textRenderer.render(
				textFormat()
						.text("Mana: " + owner.mana() + " / " + owner.maxMana())
						.font(re.font)
						.fontSize(30)
						.colour(color)
						.hAlign(LEFT)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel().copy().translate(xPos.get(), deckArea.y().get() + MANA_TEXT_Y_OFFSET)));
	}

}
