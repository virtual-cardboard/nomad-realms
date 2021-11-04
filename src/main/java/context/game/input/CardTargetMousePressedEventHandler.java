package context.game.input;

import common.GameInputEventHandler;
import context.input.event.MousePressedInputEvent;

public class CardTargetMousePressedEventHandler extends GameInputEventHandler<MousePressedInputEvent> {

	public CardTargetMousePressedEventHandler(final NomadsGameInputContext inputContext) {
		super(event -> {
			if (inputContext.targetingType == null) {
				return null;
			}
			switch (inputContext.targetingType) {
				// TODO
				case CHARACTER:
					break;
				default:
					break;
			}
			return null;
		});
	}

}
