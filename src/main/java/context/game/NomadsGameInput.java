package context.game;

import static common.event.NetworkEvent.fromPacket;

import java.util.List;

import common.GameInputEventHandler;
import common.math.PosDim;
import common.math.Vector2f;
import context.input.GameInput;
import context.visuals.gui.Gui;
import context.visuals.gui.RootGui;
import event.game.CardHoveredEvent;
import graphics.gui.CardDashboardGui;
import graphics.gui.CardGui;
import model.card.CardDashboard;

public class NomadsGameInput extends GameInput {

	private Vector2f cardMouseOffset;
	/** The card that is dragged and about to be played */
	private CardGui selected;

	@Override
	protected void init() {
		addPacketReceivedFunction(new GameInputEventHandler<>(event -> {
			return fromPacket(event.model());
		}));
		addMouseMovedFunction(new GameInputEventHandler<>(event -> {
			if (selected != null) {
				selected.setPos(cursor().cursorCoordinates().copy().sub(cardMouseOffset));
				return null;
			}
			CardGui hovered = hoveredCardGui();
			unhoverAll();
			if (hovered != null) {
				hovered.hover();
				NomadsGameData data = (NomadsGameData) context().data();
				return new CardHoveredEvent(data.player(), hovered.card());
			}
			return null;
		}));
		addMousePressedFunction(new GameInputEventHandler<>(event -> {
			CardGui hovered = hoveredCardGui();
			if (hovered != null) {
				selected = hovered;
				cardMouseOffset = hovered.posdim().pos().negate().add(cursor().cursorCoordinates());
				hovered.setDragged(true);
			}
			return null;
		}));
		addMouseReleasedFunction(new GameInputEventHandler<>(event -> {
			if (selected != null) {
				NomadsGameVisuals visuals = (NomadsGameVisuals) context().visuals();
				NomadsGameData data = (NomadsGameData) context().data();
				CardDashboard dashboard = data.state().dashboard(data.player());
				CardDashboardGui dashboardGui = visuals.getDashboardGui();
				Vector2f coords = cursor().cursorCoordinates();
				if (!validCursorCoordinates(visuals.rootGui(), coords) || hoveringOver(dashboardGui.getCardHolder(), coords)) {
					dashboardGui.resetTargetPositions(visuals.rootGui().getDimensions());
					selected.unhover();
				} else {
					int index = dashboard.hand().indexOf(selected.card().id());
					dashboardGui.removeCardGui(index);
					selected.remove();
					dashboard.hand().delete(index);
//					card.effect().target();
					// TODO
					System.out.println("played card!");
//					return new CardPlayedEvent(data.player(), card, null);
				}
			}
			selected.setDragged(false);
			selected = null;
			return null;
		}));
	}

	private void unhoverAll() {
		List<CardGui> cardGuis = ((NomadsGameVisuals) context().visuals()).getDashboardGui().cardGuis();
		for (int i = 0; i < cardGuis.size(); i++) {
			cardGuis.get(i).unhover();
		}
	}

	private CardGui hoveredCardGui() {
		NomadsGameVisuals visuals = (NomadsGameVisuals) context().visuals();
		CardDashboardGui dashboardGui = visuals.getDashboardGui();
		Vector2f cursor = cursor().cursorCoordinates();
		List<CardGui> cardGuis = dashboardGui.cardGuis();
		for (int i = 0; i < cardGuis.size(); i++) {
			CardGui cardGui = cardGuis.get(i);
			if (hoveringOverCardGui(cardGui, cursor)) {
				return cardGui;
			}
		}
		return null;
	}

	private boolean hoveringOverCardGui(CardGui gui, Vector2f cursor) {
		if (gui.parent() == null) {
			System.out.println("no parent!");
			return false;
		}
		PosDim pd = gui.posdim();
		float cx = cursor.x;
		float cy = cursor.y;
		return pd.x + pd.w * 0.09f <= cx && cx <= pd.x + pd.w * 0.89f && pd.y + pd.h * 0.165f <= cy && cy <= pd.y + pd.h * 0.82f;
	}

	private boolean hoveringOver(Gui gui, Vector2f cursor) {
		if (gui.parent() == null) {
			System.out.println("no parent!");
			return false;
		}
		PosDim pd = gui.posdim();
		float cx = cursor.x;
		float cy = cursor.y;
		return pd.x <= cx && cx <= pd.x + pd.w && pd.y <= cy && cy <= pd.y + pd.h;
	}

	private boolean validCursorCoordinates(RootGui rootGui, Vector2f cursor) {
		Vector2f dim = rootGui.getDimensions();
		return 0 <= cursor.x && cursor.x <= dim.x && 0 <= cursor.y && cursor.y <= dim.y;
	}

}
