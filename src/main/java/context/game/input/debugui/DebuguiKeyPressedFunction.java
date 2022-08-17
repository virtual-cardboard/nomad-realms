package context.game.input.debugui;

import java.util.function.Function;

import context.game.data.DebugTools;
import context.input.event.KeyPressedInputEvent;
import engine.common.event.GameEvent;

public class DebuguiKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private final DebugTools tools;

	public DebuguiKeyPressedFunction(DebugTools tools) {
		this.tools = tools;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent keyPressedInputEvent) {
		if (keyPressedInputEvent.code() == 'T') {
			tools.consoleGui.setHidden(!tools.consoleGui.isHidden());
		}
		return null;
	}

}
