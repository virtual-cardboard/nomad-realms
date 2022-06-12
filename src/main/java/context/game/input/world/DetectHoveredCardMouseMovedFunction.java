package context.game.input.world;

import java.util.function.Function;

import context.game.visuals.gui.dashboard.WorldCardGui;
import context.input.event.MouseMovedInputEvent;
import engine.common.event.GameEvent;
import event.playerinput.PlayerHoveredCardEvent;

public class DetectHoveredCardMouseMovedFunction implements Function<MouseMovedInputEvent, GameEvent> {

	private NomadsInputWorldInfo inputInfo;

	public DetectHoveredCardMouseMovedFunction(NomadsInputWorldInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MouseMovedInputEvent event) {
		if (inputInfo.selectedCardGui != null) {
			return null;
		}
		WorldCardGui hovered = inputInfo.hoveredCardGui();
		inputInfo.unhoverAllCardGuis();
		if (shouldHover(hovered)) {
			hovered.hover(inputInfo.settings);
			return new PlayerHoveredCardEvent(inputInfo.data.playerID(), hovered.cardID());
		}
		return null;
	}

	private boolean shouldHover(WorldCardGui hovered) {
		return hovered != null && inputInfo.cardWaitingForTarget == null && !hovered.lockedTargetPos();
	}

}
