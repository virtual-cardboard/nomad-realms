package context.game.input;

import java.util.List;

import common.math.PosDim;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.input.mouse.GameCursor;
import context.visuals.gui.Gui;
import context.visuals.gui.RootGui;
import event.game.logicprocessing.CardPlayedEvent;
import model.GameObject;
import model.card.GameCard;

public class NomadsGameInputContext {

	public Vector2f cardMouseOffset;
	public CardGui selectedCardGui;
	public CardGui cardWaitingForTarget;
	public NomadsGameVisuals visuals;
	public NomadsGameData data;
	public GameCursor cursor;

	public void init(NomadsGameVisuals visuals, NomadsGameData data, GameCursor cursor) {
		this.visuals = visuals;
		this.data = data;
		this.cursor = cursor;
	}

	/**
	 * Visually plays the card
	 * 
	 * @param dashboard
	 * @param dashboardGui
	 * @param card
	 * @param target
	 * @return a {@link CardPlayedEvent}
	 */
	public CardPlayedEvent playCard(GameCard card, GameObject target) {
		CardPlayedEvent cardPlayedEvent = new CardPlayedEvent(data.player(), card, target);
		// Cantrip card GUIs get moved from NomadsGameLogic
		selectedCardGui = null;
		return cardPlayedEvent;
	}

	public void unhoverAllCardGuis() {
		visuals.dashboardGui().hand().cardGuis().forEach(CardGui::unhover);
	}

	public CardGui hoveredCardGui() {
		CardDashboardGui dashboardGui = visuals.dashboardGui();
		Vector2i cursorPos = cursor.pos();
		List<CardGui> cardGuis = dashboardGui.hand().cardGuis();
		for (CardGui cardGui : cardGuis) {
			if (hoveringOverCardGui(cardGui, cursorPos)) {
				return cardGui;
			}
		}
		return null;

	}

	public boolean hoveringOver(Gui gui, Vector2i cursor) {
		PosDim pd = gui.posdim();
		float cx = cursor.x;
		float cy = cursor.y;
		return pd.x <= cx && cx <= pd.x + pd.w && pd.y <= cy && cy <= pd.y + pd.h;
	}

	public boolean hoveringOverCardGui(CardGui gui, Vector2i cursor) {
		PosDim pd = gui.posdim();
		float cx = cursor.x;
		float cy = cursor.y;
		return pd.x + pd.w * 0.09f <= cx && cx <= pd.x + pd.w * 0.89f && pd.y + pd.h * 0.165f <= cy && cy <= pd.y + pd.h * 0.82f;
	}

	public boolean validCursorCoordinates(RootGui rootGui, Vector2i cursor) {
		Vector2f dim = rootGui.dimensions();
		return 0 <= cursor.x && cursor.x <= dim.x && 0 <= cursor.y && cursor.y <= dim.y;
	}

	public GameCamera camera() {
		return visuals.camera();
	}

}
