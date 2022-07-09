package context.game.input.debugui;

import java.util.function.Function;

import context.game.NomadsGameData;
import context.input.event.KeyPressedInputEvent;
import engine.common.event.GameEvent;

public class DebuguiKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private final NomadsGameData data;

	public DebuguiKeyPressedFunction(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent keyPressedInputEvent) {
		if (keyPressedInputEvent.code() == 'T') {
			data.tools().consoleGui.setHidden(!data.tools().consoleGui.isHidden());
		}
		return null;
	}

}
