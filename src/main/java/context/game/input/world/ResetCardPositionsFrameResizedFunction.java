package context.game.input.world;

import java.util.function.Function;

import context.input.event.FrameResizedInputEvent;
import engine.common.event.GameEvent;

public class ResetCardPositionsFrameResizedFunction implements Function<FrameResizedInputEvent, GameEvent> {

	private NomadsInputWorldInfo inputInfo;

	public ResetCardPositionsFrameResizedFunction(NomadsInputWorldInfo inputInfo) {
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
