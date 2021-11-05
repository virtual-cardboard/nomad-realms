package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.CardDashboardGui;
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
		NomadsGameVisuals visuals = inputContext.visuals;
		NomadsGameData data = inputContext.data;
		CardDashboard dashboard = data.state().dashboard(data.player());
		CardDashboardGui dashboardGui = visuals.getDashboardGui();
		RootGui rootGui = visuals.rootGui();
		if (!canPlayCard(rootGui, dashboardGui, inputContext.cursor)) {
			revertCardGui(dashboardGui, rootGui.getDimensions());
			return null;
		} else {
			GameCard card = inputContext.selectedCardGui.card();
			CardTargetType target = card.effect().target;
			if (target != null) {
				return playCardWithTarget(target);
			} else {
				return playCardWithoutTarget(data, dashboard, dashboardGui, card);
			}
		}
	}

	private boolean canPlayCard(RootGui rootGui, CardDashboardGui dashboardGui, GameCursor cursor) {
		Vector2f coords = cursor.coordinates();
		return inputContext.validCursorCoordinates(rootGui, coords)
				&& !inputContext.hoveringOver(dashboardGui.getCardHolder(), coords)
				&& inputContext.targetingType == null;
	}

	private void revertCardGui(CardDashboardGui dashboardGui, Vector2f rootGuiDimensions) {
		inputContext.selectedCardGui.setLockPos(false);
		inputContext.selectedCardGui.setLockTargetPos(false);
		inputContext.selectedCardGui = null;
	}

	private GameEvent playCardWithTarget(CardTargetType target) {
		System.out.println("Played card with target: " + target);
		inputContext.selectedCardGui.setTargetPos(1800, 200);
		inputContext.selectedCardGui.setLockPos(false);
		inputContext.selectedCardGui.setLockTargetPos(true);
		inputContext.selectedCardGui = null;
		inputContext.targetingType = target;
		return null;
	}

	private GameEvent playCardWithoutTarget(NomadsGameData data, CardDashboard dashboard, CardDashboardGui dashboardGui, GameCard card) {
		int index = dashboard.hand().indexOf(card.id());
		dashboardGui.removeCardGui(index);
		inputContext.selectedCardGui.remove();
		dashboard.hand().delete(index);
		System.out.println("Played card with no target");
		return new CardPlayedEvent(data.player(), card, null);
	}

}
