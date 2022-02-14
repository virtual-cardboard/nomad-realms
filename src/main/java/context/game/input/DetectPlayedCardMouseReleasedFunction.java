package context.game.input;

import static context.game.visuals.gui.CardGui.WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import context.game.visuals.gui.CardDashboardGui;
import context.input.event.MouseReleasedInputEvent;
import context.input.mouse.GameCursor;
import context.visuals.gui.RootGui;
import model.card.WorldCard;
import model.card.expression.CardTargetType;
import model.state.GameState;

public class DetectPlayedCardMouseReleasedFunction implements Function<MouseReleasedInputEvent, GameEvent> {

	private NomadsInputInfo inputInfo;

	public DetectPlayedCardMouseReleasedFunction(NomadsInputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MouseReleasedInputEvent t) {
		if (inputInfo.selectedCardGui == null || t.button() != GLFW_MOUSE_BUTTON_LEFT) {
			return null;
		}
		CardDashboardGui dashboardGui = inputInfo.visuals.dashboardGui();
		RootGui rootGui = inputInfo.visuals.rootGui();
		if (!draggedOutOfHand(rootGui, dashboardGui, inputInfo.cursor) && !canPlayCard()) {
			revertCardGui(dashboardGui, rootGui.dimensions());
			return null;
		} else {
			WorldCard card = inputInfo.data.states().peekLast().card(inputInfo.selectedCardGui.cardID());
			CardTargetType target = card.effect().targetType;
			if (target != null) {
				return playCardWithTarget(rootGui.dimensions());
			} else {
				return playCardWithoutTarget();
			}
		}
	}

	private boolean draggedOutOfHand(RootGui rootGui, CardDashboardGui dashboardGui, GameCursor cursor) {
		Vector2f coords = inputInfo.selectedCardGui.centerPos();
		Vector2f screenDim = rootGui.dimensions();
		return inputInfo.validCursorCoordinates(rootGui, cursor.pos())
				&& coords.y < screenDim.y - 300
				&& inputInfo.cardWaitingForTarget == null;
	}

	private boolean canPlayCard() {
		long cardID = inputInfo.selectedCardGui.cardID();
		GameState state = inputInfo.data.states().peekLast();
		WorldCard card = state.card(cardID);
		return card.effect().playPredicate.test(state.cardPlayer(inputInfo.data.playerID()), state);
	}

	private void revertCardGui(CardDashboardGui dashboardGui, Vector2f rootGuiDimensions) {
		inputInfo.selectedCardGui.setLockPos(false);
		inputInfo.selectedCardGui.setLockTargetPos(false);
		inputInfo.selectedCardGui.unhover();
		inputInfo.selectedCardGui = null;
	}

	private GameEvent playCardWithTarget(Vector2f rootGuiDimensions) {
		inputInfo.selectedCardGui.setTargetPos(rootGuiDimensions.x - WIDTH * 0.5f, 200);
		inputInfo.selectedCardGui.setLockTargetPos(true);
		inputInfo.selectedCardGui.setLockPos(false);
		inputInfo.selectedCardGui.unhover();
		inputInfo.cardWaitingForTarget = inputInfo.selectedCardGui;
		inputInfo.selectedCardGui = null;
		return null;
	}

	private GameEvent playCardWithoutTarget() {
		return inputInfo.playCard(inputInfo.selectedCardGui.cardID(), null);
	}

}
