package context.game.input;

import static context.visuals.colour.Colour.rgb;

import java.util.function.Function;

import context.game.NomadsGameData;
import context.input.event.KeyPressedInputEvent;
import engine.common.event.GameEvent;
import org.lwjgl.glfw.GLFW;

public class PauseGameKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private final NomadsGameData data;

	public PauseGameKeyPressedFunction(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent event) {
		if (event.code() == GLFW.GLFW_KEY_P) {
			data.togglePause();
			data.tools().logMessage("Paused: " + data.paused(), rgb(255, 0, 0));
		}
		return null;
	}

}
