package context.game.visuals.handler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.game.visuals.gui.dashboard.WorldCardGui;
import context.visuals.gui.RootGui;
import event.logicprocessing.SpawnSelfAsyncEvent;
import model.actor.health.cardplayer.CardPlayer;
import model.card.WorldCard;

public class SpawnSelfAsyncEventVisualHandler implements Consumer<SpawnSelfAsyncEvent> {

	private final NomadsGameVisuals visuals;
	private final RootGui rootGui;
	private final NomadsGameData data;

	public SpawnSelfAsyncEventVisualHandler(NomadsGameVisuals visuals, RootGui rootGui, NomadsGameData data) {
		this.visuals = visuals;
		this.rootGui = rootGui;
		this.data = data;
	}

	@Override
	public void accept(SpawnSelfAsyncEvent e) {
		CardDashboardGui dashboardGui = visuals.dashboardGui();
		dashboardGui.setPlayerID(e.playerID());
		CardPlayer player = data.playerID().getFrom(data.nextState());
		for (WorldCard card : player.cardDashboard().hand()) {
			dashboardGui.hand().addChild(new WorldCardGui(card, data.resourcePack()));
		}
		dashboardGui.resetTargetPositions(rootGui.dimensions(), data.settings());
		dashboardGui.setEnabled(true);
	}

}
