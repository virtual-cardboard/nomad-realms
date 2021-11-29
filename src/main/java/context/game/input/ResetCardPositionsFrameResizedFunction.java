package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.game.visuals.gui.CardGui;
import context.input.event.FrameResizedInputEvent;

public class ResetCardPositionsFrameResizedFunction implements Function<FrameResizedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public ResetCardPositionsFrameResizedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(FrameResizedInputEvent t) {
		inputContext.visuals.dashboardGui().resetTargetPositions(inputContext.visuals.rootGui().dimensions());
		if (inputContext.cardWaitingForTarget != null) {
			inputContext.cardWaitingForTarget.setLockTargetPos(false);
			inputContext.cardWaitingForTarget.setTargetPos(t.width() - CardGui.WIDTH * 0.5f, 200);
		}
		return null;
	}

}
