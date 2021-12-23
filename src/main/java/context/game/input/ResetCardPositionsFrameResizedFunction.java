package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.game.visuals.gui.CardGui;
import context.input.event.FrameResizedInputEvent;

public class ResetCardPositionsFrameResizedFunction implements Function<FrameResizedInputEvent, GameEvent> {

	private NomadsGameInputInfo inputInfo;

	public ResetCardPositionsFrameResizedFunction(NomadsGameInputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(FrameResizedInputEvent t) {
		inputInfo.visuals.dashboardGui().resetTargetPositions(inputInfo.visuals.rootGui().dimensions());
		if (inputInfo.cardWaitingForTarget != null) {
			inputInfo.cardWaitingForTarget.setLockTargetPos(false);
			inputInfo.cardWaitingForTarget.setTargetPos(t.width() - CardGui.WIDTH * 0.5f, 200);
		}
		return null;
	}

}
