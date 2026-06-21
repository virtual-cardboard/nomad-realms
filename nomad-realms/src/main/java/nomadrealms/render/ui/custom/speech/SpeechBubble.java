package nomadrealms.render.ui.custom.speech;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.rendering.text.TextFormat.textFormat;

import engine.common.math.Vector2f;
import engine.visuals.rendering.text.TextFormat;
import engine.visuals.rendering.text.TextRenderer;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

/**
 * A speech bubble or box that displays dialogue or messages spoken by characters in the game.
 */
public class SpeechBubble implements UI {

	private final Actor actor;
	private String text;
	private long endTime;

	public SpeechBubble(Actor actor) {
		this.actor = actor;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (text == null || System.currentTimeMillis() >= endTime) {
			return;
		}

		float scale = re.is.camera.zoom().get();
		float fontSize = 16 * scale;
		// 30 characters max per line approx.
		// We can use TextRenderer.calculateTextSize to get dimensions.
		// A better way to "wrap to 30 chars" is to estimate pixel width.
		// Let's assume average char width is roughly 0.5 * fontSize.
		float wrapWidth = 30 * fontSize * 0.5f;

		TextFormat format = textFormat()
				.text(text)
				.font(re.font)
				.fontSize(fontSize)
				.colour(rgb(0, 0, 0))
				.lineWidth(wrapWidth);

		Vector2f textSize = TextRenderer.calculateTextSize(format);

		float padding = 10 * scale;
		float bubbleW = textSize.x() + padding * 2;
		float bubbleH = textSize.y() + padding * 2;

		Vector2f actorPos = actor.getScreenPosition(re).vector();

		float bubbleX = actorPos.x() - bubbleW / 2;
		float bubbleY = actorPos.y() - bubbleH - 30 * scale; // Positioned above head

		float borderSize = 2 * scale;

		// Render tail
		float tailSize = 15 * scale;
		re.triangleRenderer.render(
				actorPos.x() - tailSize / 2, bubbleY + bubbleH,
				actorPos.x() + tailSize / 2, bubbleY + bubbleH,
				actorPos.x(), bubbleY + bubbleH + tailSize / 3,
				rgb(255, 255, 255), rgb(0, 0, 0), borderSize
		);

		// Render bubble
		re.rectangleRenderer.render(
				bubbleX, bubbleY, bubbleW, bubbleH,
				10 * scale,
				rgb(255, 255, 255), rgb(0, 0, 0), borderSize
		);

		// Render inner triangle to cover junction
		re.triangleRenderer.render(
//				actorPos.x() - tailSize / 2 + borderSize / 2, bubbleY + bubbleH - 2 * scale,
//				actorPos.x() + tailSize / 2 - borderSize / 2, bubbleY + bubbleH - 2 * scale,
//				actorPos.x(), bubbleY + bubbleH + tailSize / 3 - borderSize / 2,
				actorPos.x() - tailSize / 2, bubbleY + bubbleH - borderSize,
				actorPos.x() + tailSize / 2, bubbleY + bubbleH - borderSize,
				actorPos.x(), bubbleY + bubbleH + tailSize / 3 - borderSize,
				rgb(255, 255, 205)
		);

		// Render text
		re.textRenderer.render(format.transform(re.textRenderer.screenToPixel()).pixelPosition(bubbleX + padding, bubbleY + padding));
	}

	public Actor actor() {
		return actor;
	}

	public void say(String text, long duration) {
		this.text = text;
		this.endTime = System.currentTimeMillis() + duration;
	}

}
