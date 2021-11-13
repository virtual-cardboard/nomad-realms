package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.input.event.FrameResizedInputEvent;

public class ResetCardPositionsFrameResizedFunction implements Function<FrameResizedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public ResetCardPositionsFrameResizedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(FrameResizedInputEvent t) {
		inputContext.visuals.dashboardGui().resetTargetPositions(inputContext.visuals.rootGui().dimensions());
		return null;
	}

}
