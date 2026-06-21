package nomadrealms.render.ui.custom.indicator;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.CustomSupplierConstraint.custom;
import static engine.visuals.rendering.text.TextFormat.textFormat;

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
	private static final int MANA_TEXT_Y_OFFSET = 20;

	private final CardPlayer owner;
	private final ConstraintBox constraintBox;
	private long lastManaErrorTime;

	public ManaIndicator(CardPlayer owner, ConstraintBox constraintBox) {
		this.owner = owner;
		this.constraintBox = constraintBox;
	}

	public void triggerError() {
		this.lastManaErrorTime = System.currentTimeMillis();
	}

	@Override
	public void render(RenderingEnvironment re) {
		long timeSinceError = System.currentTimeMillis() - lastManaErrorTime;
		int color = (timeSinceError < MANA_ERROR_ANIMATION_DURATION_MS) ? rgb(255, 0, 0) : rgb(0, 0, 0);
		Constraint xPos = constraintBox.x().add(MANA_TEXT_X_OFFSET).add(custom("shake", () -> {
			long t = System.currentTimeMillis() - lastManaErrorTime;
			return (t < MANA_ERROR_ANIMATION_DURATION_MS) ? (float) Math.sin(t / 1000.0 * MANA_ERROR_SHAKE_FREQUENCY * 2 * Math.PI) * MANA_ERROR_SHAKE_AMPLITUDE : 0;
		}));
		re.textRenderer.render(
				textFormat()
						.text("Mana: " + owner.mana() + " / " + owner.maxMana())
						.font(re.font)
						.fontSize(30)
						.colour(color)
						.hAlign(HorizontalAlign.LEFT)
						.vAlign(VerticalAlign.TOP)
						.transform(re.textRenderer.screenToPixel()).pixelPosition(xPos.get(), constraintBox.y().get() + MANA_TEXT_Y_OFFSET));
	}

}
