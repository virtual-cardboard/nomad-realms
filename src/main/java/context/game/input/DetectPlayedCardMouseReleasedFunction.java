package context.game.input;

import static context.game.visuals.gui.CardGui.WIDTH;

import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import context.game.visuals.gui.CardDashboardGui;
import context.input.event.MouseReleasedInputEvent;
import context.input.mouse.GameCursor;
import context.visuals.gui.RootGui;
import model.card.GameCard;
import model.card.effect.CardTargetType;

public class DetectPlayedCardMouseReleasedFunction implements Function<MouseReleasedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public DetectPlayedCardMouseReleasedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(MouseReleasedInputEvent t) {
		if (inputContext.selectedCardGui == null) {
			return null;
		}
		CardDashboardGui dashboardGui = inputContext.visuals.dashboardGui();
		RootGui rootGui = inputContext.visuals.rootGui();
		if (!canPlayCard(rootGui, dashboardGui, inputContext.cursor)) {
			revertCardGui(dashboardGui, rootGui.dimensions());
			return null;
		} else {
			GameCard card = inputContext.selectedCardGui.card();
			CardTargetType target = card.effect().targetType;
			if (target != null) {
				return playCardWithTarget(rootGui.dimensions());
			} else {
				return playCardWithoutTarget();
			}
		}
	}

	private boolean canPlayCard(RootGui rootGui, CardDashboardGui dashboardGui, GameCursor cursor) {
		Vector2f coords = inputContext.selectedCardGui.centerPos();
		return inputContext.validCursorCoordinates(rootGui, cursor.pos())
				&& !inputContext.hoveringOver(dashboardGui.hand(), coords)
				&& inputContext.cardWaitingForTarget == null;
	}

	private void revertCardGui(CardDashboardGui dashboardGui, Vector2f rootGuiDimensions) {
		inputContext.selectedCardGui.setLockPos(false);
		inputContext.selectedCardGui.setLockTargetPos(false);
		inputContext.selectedCardGui.unhover();
		inputContext.selectedCardGui = null;
	}

	private GameEvent playCardWithTarget(Vector2f rootGuiDimensions) {
		inputContext.selectedCardGui.setTargetPos(rootGuiDimensions.x - WIDTH * 0.5f, 200);
		inputContext.selectedCardGui.setLockTargetPos(true);
		inputContext.selectedCardGui.setLockPos(false);
		inputContext.selectedCardGui.unhover();
		inputContext.cardWaitingForTarget = inputContext.selectedCardGui;
		inputContext.selectedCardGui = null;
		return null;
	}

	private GameEvent playCardWithoutTarget() {
		return inputContext.playCard(inputContext.selectedCardGui.card(), null);
	}

}
