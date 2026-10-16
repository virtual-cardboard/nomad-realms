package nomadrealms.render.ui.custom.indicator;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.CustomSupplierConstraint.custom;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.MIDDLE;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class ManaIndicator implements UI {

	private static final long MANA_ERROR_ANIMATION_DURATION_MS = 500;
	private static final int MANA_ERROR_SHAKE_AMPLITUDE = 5;
	private static final int MANA_ERROR_SHAKE_FREQUENCY = 20;

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
		int textColor = (timeSinceError < MANA_ERROR_ANIMATION_DURATION_MS) ? rgb(255, 0, 0) : rgb(255, 255, 255);

		float margin = 20f;
		float barX = deckArea.x().get() + margin;
		float barWidth = Math.max(10f, deckArea.w().get() - 2 * margin);
		float barHeight = 14f;
		float barY = deckArea.y().get() + deckArea.h().get() - barHeight - 5f;

		Constraint xShake = custom("shake", () -> {
			long t = System.currentTimeMillis() - lastManaErrorTime;
			return (t < MANA_ERROR_ANIMATION_DURATION_MS) ? (float) Math.sin(t / 1000.0 * MANA_ERROR_SHAKE_FREQUENCY * 2 * Math.PI) * MANA_ERROR_SHAKE_AMPLITUDE : 0;
		});

		float finalBarX = barX + xShake.get();

		// Draw background horizontal bar
		re.rectangleRenderer.render(finalBarX, barY, barWidth, barHeight, 0, rgb(50, 50, 50), rgb(30, 30, 30), 1);

		// Draw filled mana bar extending horizontally from left to right
		if (owner.maxMana() > 0) {
			float manaRatio = Math.max(0f, Math.min(1f, (float) owner.mana() / owner.maxMana()));
			float fillWidth = barWidth * manaRatio;
			if (fillWidth > 0) {
				re.rectangleRenderer.render(finalBarX, barY, fillWidth, barHeight, 0, rgb(0, 150, 255));
			}
		}

		re.textRenderer.render(
				textFormat()
						.text("Mana: " + owner.mana() + " / " + owner.maxMana())
						.font(re.font)
						.fontSize(16)
						.colour(textColor)
						.hAlign(CENTER)
						.vAlign(MIDDLE)
						.transform(re.textRenderer.screenToPixel().copy().translate(finalBarX + barWidth * 0.5f, barY + barHeight * 0.5f)));
	}

}
