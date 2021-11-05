package context.game;

import static common.event.NetworkEvent.fromPacket;

import java.util.List;

import common.GameInputEventHandler;
import common.math.PosDim;
import common.math.Vector2f;
import context.game.input.DetectPlayedCardMouseReleasedFunction;
import context.game.input.NomadsGameInputContext;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.input.GameInput;
import event.game.CardHoveredEvent;

public class NomadsGameInput extends GameInput {

	private NomadsGameInputContext inputContext = new NomadsGameInputContext();

	@Override
	protected void init() {
		inputContext.init((NomadsGameVisuals) context().visuals(), (NomadsGameData) context().data(), cursor());
		addPacketReceivedFunction(new GameInputEventHandler<>(event -> {
			return fromPacket(event.model());
		}));
		addMouseMovedFunction(new GameInputEventHandler<>(event -> {
			if (inputContext.selectedCardGui != null) {
				inputContext.selectedCardGui.setPos(cursor().coordinates().copy().sub(inputContext.cardMouseOffset));
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
			unhoverAll();
			if (hovered != null) {
				hovered.hover();
				inputContext.selectedCardGui = hovered;
				inputContext.cardMouseOffset = hovered.posdim().pos().negate().add(cursor().coordinates());
				hovered.setLockPos(true);
			}
			return null;
		}));
		addMouseReleasedFunction(new GameInputEventHandler<>(new DetectPlayedCardMouseReleasedFunction(inputContext)));
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
		Vector2f cursor = cursor().coordinates();
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

}
