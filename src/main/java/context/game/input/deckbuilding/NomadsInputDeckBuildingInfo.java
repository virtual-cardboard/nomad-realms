package context.game.input.deckbuilding;

import app.NomadsSettings;
import common.math.PosDim;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.NomadsGameData;
import context.game.NomadsGameVisuals;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.deckbuilding.CollectionCardGui;
import context.input.mouse.GameCursor;
import context.visuals.gui.Gui;
import context.visuals.gui.RootGui;

public class NomadsInputDeckBuildingInfo {

	public Vector2f cardMouseOffset;
	public CollectionCardGui selectedCardGui;
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

	public void unhoverAllCardGuis() {
		visuals.deckBuildingGui().cardGuis().forEach(cardGui -> cardGui.unhover(settings));
	}

	public CollectionCardGui hoveredCardGui() {
		Gui hovered = getHoveredGui(cursor.pos());
		if (hovered instanceof CollectionCardGui) {
			return (CollectionCardGui) hovered;
		}
		return null;
	}

	public boolean hoveringOver(Gui gui) {
		return hoveringOver(gui, cursor.pos());
	}

	public boolean hoveringOver(Gui gui, Vector2i coords) {
		return hoveringOver(gui, coords.toVec2f());
	}

	public boolean hoveringOver(Gui gui, Vector2f coords) {
		PosDim pd = gui.posdim();
		float cx = coords.x;
		float cy = coords.y;
		return pd.x <= cx && cx <= pd.x + pd.w && pd.y <= cy && cy <= pd.y + pd.h;
	}

	public Gui getHoveredGui(Vector2i coords) {
		return visuals.rootGui().getHoveredGui(coords);
	}

	public boolean validCursorCoordinates(RootGui rootGui, Vector2i cursor) {
		Vector2f dim = rootGui.dimensions();
		return 0 <= cursor.x && cursor.x <= dim.x && 0 <= cursor.y && cursor.y <= dim.y;
	}

	public GameCamera camera() {
		return visuals.camera();
	}

}
