package context.game.input.world;

import java.util.List;

import app.NomadsSettings;
import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.game.visuals.gui.dashboard.WorldCardGui;
import context.input.mouse.GameCursor;
import context.visuals.gui.Gui;
import context.visuals.gui.RootGui;
import engine.common.math.PosDim;
import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import event.logicprocessing.CardPlayedEvent;
import model.GameObject;
import model.card.WorldCard;
import model.id.WorldCardId;

public class NomadsInputWorldInfo {

	public Vector2f cardMouseOffset;
	public WorldCardGui selectedCardGui;
	public WorldCardGui cardWaitingForTarget;
	public NomadsGameVisuals visuals;
	public NomadsGameData data;
	public NomadsSettings settings;
	public GameCursor cursor;

	public void init(NomadsGameVisuals visuals, NomadsGameData data, GameCursor cursor) {
		this.visuals = visuals;
		this.data = data;
		this.settings = data.settings();
		this.cursor = cursor;
	}

	public CardPlayedEvent playCard(WorldCardId cardID, GameObject target) {
		CardPlayedEvent cardPlayedEvent = new CardPlayedEvent(data.playerID(), target != null ? target.id() : null, cardID);
		selectedCardGui = null;
		return cardPlayedEvent;
	}

	public WorldCard card(WorldCardId cardID) {
		return cardID.getFrom(data.currentState());
	}

	public void unhoverAllCardGuis() {
		visuals.dashboardGui().hand().cardGuis().forEach(cardGui -> cardGui.unhover(settings));
		visuals.dashboardGui().hand().resetTargetPositions(visuals.rootGui().dimensions(), settings);
	}

	public WorldCardGui hoveredCardGui() {
		CardDashboardGui dashboardGui = visuals.dashboardGui();
		Vector2i cursorPos = cursor.pos();
		List<WorldCardGui> cardGuis = dashboardGui.hand().cardGuis();
		for (int i = cardGuis.size() - 1; i >= 0; i--) {
			WorldCardGui cardGui = cardGuis.get(i);
			if (hoveringOver(cardGui, cursorPos)) {
				return cardGui;
			}
		}
		return null;
	}

	public boolean hoveringOver(Gui gui, Vector2i coords) {
		PosDim pd = gui.posdim();
		float cx = coords.x();
		float cy = coords.y();
		return pd.x <= cx && cx <= pd.x + pd.w && pd.y <= cy && cy <= pd.y + pd.h;
	}

	public Gui getHoveredGui(Vector2i coords) {
		return visuals.rootGui().getHoveredGui(coords);
	}

	public boolean validCursorCoordinates(RootGui rootGui, Vector2i cursor) {
		Vector2f dim = rootGui.dimensions();
		return 0 <= cursor.x() && cursor.x() <= dim.x() && 0 <= cursor.y() && cursor.y() <= dim.y();
	}

	public GameCamera camera() {
		return visuals.camera();
	}

}
