package context.game.visuals.gui.dashboard;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.data.GameData;
import context.game.NomadsGameData;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.Gui;
import model.id.CardPlayerID;
import model.state.GameState;

public abstract class CardZoneGui extends Gui {

	private CardDashboardGui cardDashboardGui;
	private List<CardGui> cardGuis = new ArrayList<>();

	@Override
	public final void render(GLContext glContext, GameData data, float x, float y, float w, float h) {
		NomadsGameData nomadsData = (NomadsGameData) data;
		NomadsSettings s = nomadsData.settings();
		GameState state = nomadsData.previousState();
		doRender(glContext, s, state, x, y, w, h);
		for (CardGui cardGui : cardGuis) {
			cardGui.render(glContext, s, state);
		}
	}

	protected abstract void doRender(GLContext glContext, NomadsSettings settings, GameState previousState, float x, float y, float w, float h);

	public List<CardGui> cardGuis() {
		return cardGuis;
	}

	public CardGui cardGui(int index) {
		return cardGuis.get(index);
	}

	public void addCardGui(CardGui cardGui) {
		cardGuis.add(cardGui);
		cardDashboardGui.putCardGui(cardGui.cardID(), cardGui);
	}

	public void addCardGui(int i, CardGui cardGui) {
		cardGuis.add(i, cardGui);
		cardDashboardGui.putCardGui(cardGui.cardID(), cardGui);
	}

	public void removeCardGui(CardGui cardGui) {
		cardDashboardGui.removeCardGui(cardGui.cardID());
		cardGuis.remove(cardGui);
	}

	public CardGui removeCardGui(int index) {
		CardGui removed = cardGuis.remove(index);
		cardDashboardGui.removeCardGui(removed.cardID());
		return removed;
	}

	public void updateCardPositions() {
		for (CardGui cardGui : cardGuis()) {
			cardGui.updatePosDim();
		}
	}

	public void resetTargetPositions(Vector2f screenDimensions, NomadsSettings settings) {
		Vector2f centerPos = centerPos(screenDimensions);
		List<CardGui> cardGuis = cardGuis();
		for (int i = 0; i < cardGuis.size(); i++) {
			if (cardGuis.get(i).lockedTargetPos()) {
				continue;
			}
			cardGuis.get(i).setTargetPos(centerPos.x, centerPos.y);
		}
	}

	public Vector2f topLeftPos(Vector2f screenDimensions) {
		return new Vector2f(posX().calculateValue(0, screenDimensions.x), posY().calculateValue(0, screenDimensions.y));
	}

	public Vector2f centerPos(Vector2f screenDimensions) {
		Vector2f dim = new Vector2f(width().calculateValue(0, screenDimensions.x), height().calculateValue(0, screenDimensions.y));
		return topLeftPos(screenDimensions).add(dim.scale(0.5f));
	}

	@Override
	public void setParent(Gui parent) {
		super.setParent(parent);
		this.cardDashboardGui = (CardDashboardGui) parent;
	}

	public CardPlayerID playerID() {
		return cardDashboardGui.playerID();
	}

}
