package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.CardDashboardGui;
import context.input.event.MousePressedInputEvent;
import event.game.CardPlayedEvent;
import model.card.CardDashboard;
import model.card.GameCard;

public class CardTargetMousePressedFunction implements Function<MousePressedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public CardTargetMousePressedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent t) {
		if (inputContext.cardWaitingForTarget == null) {
			return null;
		}
		GameCard card = inputContext.cardWaitingForTarget.card();
		switch (card.effect().target) {
			// TODO
			case CHARACTER:
				break;
			case TILE:
				NomadsGameVisuals visuals = inputContext.visuals;
				NomadsGameData data = inputContext.data;
				CardDashboard dashboard = data.state().dashboard(data.player());
				CardDashboardGui dashboardGui = visuals.getDashboardGui();
				System.out.println("Tile targeted");
				playCard(dashboard, dashboardGui, card);
				inputContext.cardWaitingForTarget = null;
				return new CardPlayedEvent(data.player(), card, null);
			default:
				break;
		}
		return null;
	};

	private GameEvent playCard(CardDashboard dashboard, CardDashboardGui dashboardGui, GameCard card) {
		int index = dashboard.hand().indexOf(card.id());
		dashboardGui.handHolder().removeCardGui(index);
		dashboard.hand().delete(index);
		dashboard.discard().addTop(card);
		inputContext.visuals.getDashboardGui().resetTargetPositions(inputContext.visuals.rootGui().getDimensions());
		return new CardPlayedEvent(inputContext.data.player(), card, null);
	}

}
