package context.game.input;

import java.util.function.Function;

import context.game.NomadsGameVisuals;
import context.input.event.KeyPressedInputEvent;
import engine.common.event.GameEvent;

public class ToggleInventoryKeyPressedFunction implements Function<KeyPressedInputEvent, GameEvent> {

	private final NomadsGameVisuals visuals;

	public ToggleInventoryKeyPressedFunction(NomadsGameVisuals visuals) {
		this.visuals = visuals;
	}

	@Override
	public GameEvent apply(KeyPressedInputEvent keyPressedInputEvent) {
		if (keyPressedInputEvent.code() == 'E') {
			visuals.inventoryGui().toggleEnabled();
		}
		return null;
	}

}
