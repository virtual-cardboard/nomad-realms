package context.game.visuals.gui;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import common.math.Matrix4f;
import common.math.PosDim;
import common.math.Vector2f;
import common.math.Vector2i;
import context.GLContext;
import context.visuals.gui.constraint.dimension.GuiDimensionConstraint;
import context.visuals.gui.constraint.position.GuiPositionConstraint;
import model.actor.CardPlayer;
import model.id.ID;
import model.state.GameState;

public abstract class CardZoneGui {

	private CardDashboardGui cardDashboardGui;
	private List<CardGui> cardGuis = new ArrayList<>();

	private GuiPositionConstraint posX;
	private GuiPositionConstraint posY;
	private GuiDimensionConstraint width;
	private GuiDimensionConstraint height;

	public final void doRender(GLContext glContext, NomadsSettings s, GameState state, float x, float y, float w, float h) {
		render(glContext, s, state, x, y, w, h);
		for (CardGui cardGui : cardGuis) {
			cardGui.render(glContext, s, state);
		}
	}

	public abstract void render(GLContext glContext, NomadsSettings s, GameState state, float x, float y, float w, float h);

	protected final Matrix4f rectToPixelMatrix4f(Vector2i screenDim) {
		return new Matrix4f().translate(new Vector2f(-1, 1)).scale(2f / screenDim.x, -2f / screenDim.y);
	}

	protected PosDim posdim() {
		PosDim p = cardDashboardGui.posdim();
		return new PosDim(posX.calculateValue(p.x, p.x + p.w), posY.calculateValue(p.y, p.y + p.h),
				width.calculateValue(p.x, p.x + p.w), height.calculateValue(p.y, p.y + p.h));
	}

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
		return new Vector2f(posX.calculateValue(0, screenDimensions.x), posY.calculateValue(0, screenDimensions.y));
	}

	public Vector2f centerPos(Vector2f screenDimensions) {
		Vector2f dim = new Vector2f(width.calculateValue(0, screenDimensions.x), height.calculateValue(0, screenDimensions.y));
		return topLeftPos(screenDimensions).add(dim.scale(0.5f));
	}

	public void setParent(CardDashboardGui cardDashboardGui) {
		this.cardDashboardGui = cardDashboardGui;
	}

	public GuiPositionConstraint posX() {
		return posX;
	}

	public void setPosX(GuiPositionConstraint posX) {
		this.posX = posX;
	}

	public GuiPositionConstraint posY() {
		return posY;
	}

	public void setPosY(GuiPositionConstraint posY) {
		this.posY = posY;
	}

	public GuiDimensionConstraint width() {
		return width;
	}

	public void setWidth(GuiDimensionConstraint width) {
		this.width = width;
	}

	public GuiDimensionConstraint height() {
		return height;
	}

	public ID<? extends CardPlayer> playerID() {
		return cardDashboardGui.playerID();
	}

	public void setHeight(GuiDimensionConstraint height) {
		this.height = height;
	}

}
