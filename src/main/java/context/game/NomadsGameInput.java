package context.game;

import static common.event.NetworkEvent.fromPacket;

import java.util.List;

import common.GameInputEventHandler;
import common.coordinates.IntCoordinates;
import common.math.PosDim;
import context.input.GameInput;
import graphics.gui.CardDashboardGui;
import graphics.gui.CardGui;

public class NomadsGameInput extends GameInput {

	private CardGui hovered;

	@Override
	protected void init() {
		addPacketReceivedFunction(new GameInputEventHandler<>(event -> {
			return fromPacket(event.model());
		}));
		addMouseMovedFunction(new GameInputEventHandler<>(event -> {
			NomadsGameVisuals visuals = (NomadsGameVisuals) context().visuals();
			CardDashboardGui dashboardGui = visuals.getDashboardGui();
			IntCoordinates cursorCoordinates = cursor().getCursorCoordinates();
			List<CardGui> cardGuis = dashboardGui.cardGuis();
			for (int i = 0; i < cardGuis.size(); i++) {
				CardGui cardGui = cardGuis.get(i);
				if (hoveringOver(cardGui, cursorCoordinates)) {
					System.out.println("yay");
					cardGui.hover();
					hovered = cardGui;
					return null;
				} else {
					cardGui.unhover();
				}
			}
			if (hovered != null) {
				System.out.println("Unhovered from " + hovered.getCard().name());
			}
			hovered = null;
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
