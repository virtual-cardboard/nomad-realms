package context.game.visuals.gui.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import app.NomadsSettings;
import context.GLContext;
import context.data.GameData;
import context.game.NomadsGameData;
import context.visuals.gui.Gui;
import engine.common.math.Vector2f;
import model.id.CardPlayerId;
import model.state.GameState;

public abstract class CardZoneGui extends Gui {

	@Override
	public final void render(GLContext glContext, GameData data, float x, float y, float w, float h) {
		NomadsGameData nomadsData = (NomadsGameData) data;
		NomadsSettings s = nomadsData.settings();
		GameState state = nomadsData.currentState();
		if (playerID().getFrom(state) != null) {
			doRender(glContext, s, state, x, y, w, h);
		}
	}

	protected abstract void doRender(GLContext glContext, NomadsSettings settings, GameState previousState, float x, float y, float w, float h);

	public List<WorldCardGui> cardGuis() {
		return getChildren().stream().map(g -> (WorldCardGui) g).collect(Collectors.toList());
	}

	@Override
	public void addChild(Gui child) {
		super.addChild(child);
		WorldCardGui cardGui = (WorldCardGui) child;
		dashboardGui().putCardGui(cardGui.cardID(), cardGui);
	}

	@Override
	public void addChild(int i, Gui child) {
		super.addChild(i, child);
		WorldCardGui cardGui = (WorldCardGui) child;
		dashboardGui().putCardGui(cardGui.cardID(), cardGui);
	}

	@Override
	public void removeChild(Gui child) {
		super.removeChild(child);
		dashboardGui().removeCardGui(((WorldCardGui) child).cardID());
	}

	@Override
	public Gui removeChild(int i) {
		Gui child = super.removeChild(i);
		dashboardGui().removeCardGui(((WorldCardGui) child).cardID());
		return child;
	}

	public boolean contains(WorldCardGui cardGui) {
		for (Gui c : getChildren()) {
			if (c == cardGui) {
				return true;
			}
		}
		return false;
	}

	public void resetTargetPositions(Vector2f screenDimensions, NomadsSettings settings) {
		Vector2f centerPos = centerPos(screenDimensions);
		List<Gui> cardGuis = getChildren();
		for (Gui cardGui : cardGuis) {
			WorldCardGui wcg = (WorldCardGui) cardGui;
			if (wcg.lockedTargetPos()) {
				continue;
			}
			wcg.setTargetPos(centerPos.x(), centerPos.y());
		}
	}

	public Vector2f topLeftPos(Vector2f screenDimensions) {
		return new Vector2f(posX().get(0, screenDimensions.x()), posY().get(0, screenDimensions.y()));
	}

	public Vector2f centerPos(Vector2f screenDimensions) {
		Vector2f dim = new Vector2f(width().get(0, screenDimensions.x()), height().get(0, screenDimensions.y()));
		return topLeftPos(screenDimensions).add(dim.scale(0.5f));
	}

	/**
	 * Because the parent GUI is always of type {@link CardDashboardGui}, it is
	 * recommended to call {@link dashboardGui} instead of this method for the sake
	 * of code clarity. Both methods do the same thing.
	 * <p>
	 * The {@code parent} function still needs to exist for game engine internal
	 * handling, but as Nomad Realms programmers, we should always call
	 * {@code dashboardGui} instead.
	 */
	@Override
	public CardDashboardGui parent() {
		return (CardDashboardGui) super.parent();
	}

	/**
	 * @return the parent {@link CardDashboardGui}
	 */
	public CardDashboardGui dashboardGui() {
		return (CardDashboardGui) super.parent();
	}

	@Override
	public void setParent(Gui parent) {
		if (parent instanceof CardDashboardGui) {
			super.setParent(parent);
		} else {
			throw new IllegalArgumentException(parent.getClass().getSimpleName() + " cannot be the parent "
					+ "of a CardZoneGui. The parent of a CardZoneGui must be a CardDashboardGui.");
		}
	}

	public CardPlayerId playerID() {
		return dashboardGui().playerID();
	}

}
