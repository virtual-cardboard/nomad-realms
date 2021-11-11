package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.input.event.KeyPressedInputEvent;

public class SwitchNomadKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public SwitchNomadKeyPressedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent t) {
		inputContext.data.setPlayer(inputContext.data.state().cardPlayer((inputContext.data.player().id() + 1) % 2));
		return null;
	}

}
