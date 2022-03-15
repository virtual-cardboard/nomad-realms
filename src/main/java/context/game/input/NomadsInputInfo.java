package context.game.input;

import java.util.List;

import app.NomadsSettings;
import common.math.PosDim;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.CardGui;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.input.mouse.GameCursor;
import context.visuals.gui.Gui;
import context.visuals.gui.RootGui;
import event.game.logicprocessing.CardPlayedEvent;
import model.GameObject;
import model.card.WorldCard;
import model.id.WorldCardID;

public class NomadsInputInfo {

	public Vector2f cardMouseOffset;
	public CardGui selectedCardGui;
	public CardGui cardWaitingForTarget;
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

	public CardPlayedEvent playCard(WorldCardID cardID, GameObject target) {
		CardPlayedEvent cardPlayedEvent = new CardPlayedEvent(data.playerID(), target != null ? target.id() : null, cardID);
		selectedCardGui = null;
		return cardPlayedEvent;
	}

	public WorldCard card(WorldCardID cardID) {
		return cardID.getFrom(data.previousState());
	}

	public void unhoverAllCardGuis() {
		visuals.dashboardGui().hand().cardGuis().forEach(cardGui -> cardGui.unhover(settings));
		visuals.dashboardGui().hand().resetTargetPositions(visuals.rootGui().dimensions(), settings);
	}

	public CardGui hoveredCardGui() {
		CardDashboardGui dashboardGui = visuals.dashboardGui();
		Vector2i cursorPos = cursor.pos();
		List<CardGui> cardGuis = dashboardGui.hand().cardGuis();
		for (int i = cardGuis.size() - 1; i >= 0; i--) {
			CardGui cardGui = cardGuis.get(i);
			if (hoveringOverCardGui(cardGui, cursorPos)) {
				return cardGui;
			}
		}
		return null;
	}

	public boolean hoveringOver(Gui gui, Vector2f coords) {
		PosDim pd = gui.posdim();
		float cx = coords.x;
		float cy = coords.y;
		return pd.x <= cx && cx <= pd.x + pd.w && pd.y <= cy && cy <= pd.y + pd.h;
	}

	public boolean hoveringOverCardGui(CardGui gui, Vector2i cursor) {
		PosDim pd = gui.posdim(settings);
		float cx = cursor.x;
		float cy = cursor.y;
		return pd.x <= cx && cx <= pd.x + pd.w && pd.y <= cy && cy <= pd.y + pd.h;
	}

	public boolean validCursorCoordinates(RootGui rootGui, Vector2i cursor) {
		Vector2f dim = rootGui.dimensions();
		return 0 <= cursor.x && cursor.x <= dim.x && 0 <= cursor.y && cursor.y <= dim.y;
	}

	public GameCamera camera() {
		return visuals.camera();
	}

}
