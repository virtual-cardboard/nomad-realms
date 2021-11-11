package context.game.input;

import java.util.List;

import common.math.PosDim;
import common.math.Vector2f;
import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.game.visuals.gui.CardZoneGui;
import context.input.mouse.GameCursor;
import context.visuals.gui.Gui;
import context.visuals.gui.RootGui;

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

	public void unhoverAllCardGuis() {
		List<CardZoneGui> cardGuis = visuals.getDashboardGui().cardZoneGuis();
		for (int i = 0; i < cardGuis.size(); i++) {
			cardGuis.get(i).cardGuis().forEach(CardGui::unhover);
		}
	}

	public CardGui hoveredCardGui() {
		CardDashboardGui dashboardGui = visuals.getDashboardGui();
		Vector2f cursorPos = cursor.pos();
		List<CardZoneGui> cardZoneGuis = dashboardGui.cardZoneGuis();
		for (int i = 0; i < cardZoneGuis.size(); i++) {
			for (CardGui cardGui : cardZoneGuis.get(i).cardGuis()) {
				if (hoveringOverCardGui(cardGui, cursorPos)) {
					return cardGui;
				}
			}
		}
		return null;
	}

	public boolean hoveringOver(Gui gui, Vector2f cursor) {
		if (gui.parent() == null) {
			System.out.println("no parent!");
			return false;
		}
		PosDim pd = gui.posdim();
		float cx = cursor.x;
		float cy = cursor.y;
		return pd.x <= cx && cx <= pd.x + pd.w && pd.y <= cy && cy <= pd.y + pd.h;
	}

	public boolean hoveringOverCardGui(CardGui gui, Vector2f cursor) {
		if (gui.parent() == null) {
			System.out.println("no parent!");
			return false;
		}
		PosDim pd = gui.posdim();
		float cx = cursor.x;
		float cy = cursor.y;
		return pd.x + pd.w * 0.09f <= cx && cx <= pd.x + pd.w * 0.89f && pd.y + pd.h * 0.165f <= cy && cy <= pd.y + pd.h * 0.82f;
	}

	public boolean validCursorCoordinates(RootGui rootGui, Vector2f cursor) {
		Vector2f dim = rootGui.getDimensions();
		return 0 <= cursor.x && cursor.x <= dim.x && 0 <= cursor.y && cursor.y <= dim.y;
	}

}
