package context.game;

import static common.event.NetworkEvent.fromPacket;

import java.util.List;

import common.GameInputEventHandler;
import common.coordinates.IntCoordinates;
import common.math.PosDim;
import context.input.GameInput;
import event.game.CardHoveredEvent;
import graphics.gui.CardDashboardGui;
import graphics.gui.CardGui;

public class NomadsGameInput extends GameInput {

	private CardGui hovered;
	/** The card that is dragged and about to be played */
	private CardGui selected;

	@Override
	protected void init() {
		addPacketReceivedFunction(new GameInputEventHandler<>(event -> {
			return fromPacket(event.model());
		}));
		addMouseMovedFunction(new GameInputEventHandler<>(event -> {
			if (selected != null) {
				selected.setPos(cursor().getCursorCoordinates());
				return null;
			}
			NomadsGameVisuals visuals = (NomadsGameVisuals) context().visuals();
			CardDashboardGui dashboardGui = visuals.getDashboardGui();
			IntCoordinates cursorCoordinates = cursor().getCursorCoordinates();
			List<CardGui> cardGuis = dashboardGui.cardGuis();
			for (int i = 0; i < cardGuis.size(); i++) {
				CardGui cardGui = cardGuis.get(i);
				if (hoveringOver(cardGui, cursorCoordinates)) {
					cardGui.hover();
					hovered = cardGui;
					NomadsGameData data = (NomadsGameData) context().data();
					return new CardHoveredEvent(data.player(), cardGui.card());
				} else {
					cardGui.unhover();
				}
			}
			hovered = null;
			return null;
		}));
		addMousePressedFunction(new GameInputEventHandler<>(event -> {
			if (hovered != null) {
				selected = hovered;
			}
			return null;
		}));
		addMouseReleasedFunction(new GameInputEventHandler<>(event -> {
			selected = null;
			return null;
		}));
	}

	private boolean hoveringOver(CardGui cardGui, IntCoordinates cursor) {
		PosDim pd = cardGui.posdim();
		int cx = cursor.x;
		int cy = cursor.y;
		return pd.x + pd.w * 0.09f <= cx && cx <= pd.x + pd.w * 0.89f && pd.y + pd.h * 0.165f <= cy && cy <= pd.y + pd.h * 0.82f;
	}

}
