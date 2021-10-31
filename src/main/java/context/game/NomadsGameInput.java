package context.game;

import static common.event.NetworkEvent.fromPacket;

import java.util.List;

import common.GameInputEventHandler;
import common.math.PosDim;
import common.math.Vector2f;
import context.input.GameInput;
import event.game.CardHoveredEvent;
import graphics.gui.CardDashboardGui;
import graphics.gui.CardGui;

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
			if (hovered != null) {
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
			}
			return null;
		}));
		addMouseReleasedFunction(new GameInputEventHandler<>(event -> {
			selected = null;
			return null;
		}));
	}

	private CardGui hoveredCardGui() {
		NomadsGameVisuals visuals = (NomadsGameVisuals) context().visuals();
		CardDashboardGui dashboardGui = visuals.getDashboardGui();
		Vector2f cursor = cursor().cursorCoordinates();
		List<CardGui> cardGuis = dashboardGui.cardGuis();
		for (int i = 0; i < cardGuis.size(); i++) {
			CardGui cardGui = cardGuis.get(i);
			if (hoveringOver(cardGui, cursor)) {
				cardGui.hover();
				return cardGui;
			} else {
				cardGui.unhover();
			}
		}
		return null;
	}

	private boolean hoveringOver(CardGui cardGui, Vector2f cursor) {
		PosDim pd = cardGui.posdim();
		float cx = cursor.x;
		float cy = cursor.y;
		return pd.x + pd.w * 0.09f <= cx && cx <= pd.x + pd.w * 0.89f && pd.y + pd.h * 0.165f <= cy && cy <= pd.y + pd.h * 0.82f;
	}

}
