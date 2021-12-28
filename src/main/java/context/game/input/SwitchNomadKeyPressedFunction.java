package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.input.event.KeyPressedInputEvent;

public class SwitchNomadKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private NomadsInputInfo inputInfo;

	public SwitchNomadKeyPressedFunction(NomadsInputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent t) {
		inputInfo.data.setPlayer(inputInfo.data.state().cardPlayer((inputInfo.data.player().id() + 1) % 2));
		return null;
	}

}
