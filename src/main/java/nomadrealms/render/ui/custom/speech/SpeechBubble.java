package nomadrealms.render.ui.custom.speech;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

/**
 * A speech bubble or box that displays dialogue or messages spoken by characters in the game.
 */
public class SpeechBubble implements UI {

	private final Actor actor;
	private boolean visible = false;

	public SpeechBubble(Actor actor) {
		this.actor = actor;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (visible) {
			re.textRenderer.alignCenterHorizontal();
		}
	}

	public Actor actor() {
		return actor;
	}

	public SpeechBubble enable() {
		this.visible = true;
		return this;
	}

	public SpeechBubble disable() {
		this.visible = false;
		return this;
	}

}
