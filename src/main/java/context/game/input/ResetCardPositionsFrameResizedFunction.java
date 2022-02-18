package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.input.event.FrameResizedInputEvent;

public class ResetCardPositionsFrameResizedFunction implements Function<FrameResizedInputEvent, GameEvent> {

	private NomadsInputInfo inputInfo;

	public ResetCardPositionsFrameResizedFunction(NomadsInputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(FrameResizedInputEvent t) {
		inputInfo.visuals.dashboardGui().resetTargetPositions(inputInfo.visuals.rootGui().dimensions(), inputInfo.settings);
		if (inputInfo.cardWaitingForTarget != null) {
			inputInfo.cardWaitingForTarget.setLockTargetPos(false);
			inputInfo.cardWaitingForTarget.setTargetPos(t.width() - inputInfo.settings.cardWidth() * 0.5f, 200);
		}
		return null;
	}

}
