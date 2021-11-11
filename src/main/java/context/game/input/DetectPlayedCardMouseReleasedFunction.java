package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.input.event.MouseReleasedInputEvent;
import context.input.mouse.GameCursor;
import context.visuals.gui.RootGui;
import event.game.CardPlayedEvent;
import model.card.CardDashboard;
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
		CardDashboard dashboard = inputContext.data.state().dashboard(inputContext.data.player());
		CardDashboardGui dashboardGui = inputContext.visuals.getDashboardGui();
		RootGui rootGui = inputContext.visuals.rootGui();
		if (!canPlayCard(rootGui, dashboardGui, inputContext.cursor)) {
			revertCardGui(dashboardGui, rootGui.getDimensions());
			return null;
		} else {
			GameCard card = inputContext.selectedCardGui.card();
			CardTargetType target = card.effect().target;
			if (target != null) {
				return playCardWithTarget();
			} else {
				return playCardWithoutTarget(dashboard, dashboardGui, card);
			}
		}
	}

	private boolean canPlayCard(RootGui rootGui, CardDashboardGui dashboardGui, GameCursor cursor) {
		Vector2f coords = cursor.pos();
		return inputContext.validCursorCoordinates(rootGui, coords)
				&& !inputContext.hoveringOver(dashboardGui.hand(), coords)
				&& inputContext.cardWaitingForTarget == null;
	}

	private void revertCardGui(CardDashboardGui dashboardGui, Vector2f rootGuiDimensions) {
		inputContext.selectedCardGui.setLockPos(false);
		inputContext.selectedCardGui.setLockTargetPos(false);
		inputContext.selectedCardGui = null;
	}

	private GameEvent playCardWithTarget() {
		System.out.println("Played card with target");
		inputContext.selectedCardGui.setTargetPos(1800, 200);
		inputContext.selectedCardGui.setLockPos(false);
		inputContext.selectedCardGui.setLockTargetPos(true);
		inputContext.cardWaitingForTarget = inputContext.selectedCardGui;
		inputContext.selectedCardGui = null;
		return null;
	}

	private GameEvent playCardWithoutTarget(CardDashboard dashboard, CardDashboardGui dashboardGui, GameCard card) {
		System.out.println("Played card with no target");
		int index = dashboard.hand().indexOf(card.id());
		CardGui cardGui = dashboardGui.hand().removeCardGui(index);
		dashboard.hand().delete(index);
		dashboardGui.discard().addCardGui(cardGui);
		dashboard.discard().addTop(card);
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		inputContext.visuals.getDashboardGui().resetTargetPositions(inputContext.visuals.rootGui().getDimensions());
		inputContext.selectedCardGui = null;
		return new CardPlayedEvent(inputContext.data.player(), card, null);
	}

}
