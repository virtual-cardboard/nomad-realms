package context.game.input;

import java.util.function.Function;

import common.event.GameEvent;
import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.CardDashboardGui;
import context.input.event.MousePressedInputEvent;
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
				CardDashboardGui dashboardGui = visuals.dashboardGui();
				System.out.println("Tile targeted");
				inputContext.cardWaitingForTarget = null;
				return inputContext.playCard(dashboard, dashboardGui, card, null);
			default:
				break;
		}
		return null;
	};

}
