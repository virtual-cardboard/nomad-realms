package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.input.event.KeyPressedInputEvent;

public class SwitchNomadKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private NomadsGameInputInfo inputInfo;

	public SwitchNomadKeyPressedFunction(NomadsGameInputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent t) {
		inputInfo.data.setPlayer(inputInfo.data.state().cardPlayer((inputInfo.data.player().id() + 1) % 2));
		return null;
	}

}
