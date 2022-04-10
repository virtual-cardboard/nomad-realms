package context.game.input.world;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.function.Function;

import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.input.event.MouseReleasedInputEvent;
import context.input.mouse.GameCursor;
import context.visuals.gui.RootGui;
import engine.common.event.GameEvent;
import engine.common.math.Vector2f;
import model.card.WorldCard;
import model.card.expression.CardTargetType;

public class DetectPlayedCardMouseReleasedFunction implements Function<MouseReleasedInputEvent, GameEvent> {

	private NomadsInputWorldInfo inputInfo;

	public DetectPlayedCardMouseReleasedFunction(NomadsInputWorldInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MouseReleasedInputEvent t) {
		if (inputInfo.selectedCardGui == null || t.button() != GLFW_MOUSE_BUTTON_LEFT) {
			return null;
		}
		CardDashboardGui dashboardGui = inputInfo.visuals.dashboardGui();
		RootGui rootGui = inputInfo.visuals.rootGui();
		if (!draggedOutOfHand(rootGui, dashboardGui, inputInfo.cursor)) {
			revertCardGui(dashboardGui, rootGui.dimensions());
			return null;
		} else {
			WorldCard card = inputInfo.selectedCardGui.cardID().getFrom(inputInfo.data.previousState());
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

	private void revertCardGui(CardDashboardGui dashboardGui, Vector2f rootGuiDimensions) {
		inputInfo.selectedCardGui.setLockPos(false);
		inputInfo.selectedCardGui.setLockTargetPos(false);
		inputInfo.selectedCardGui.unhover(inputInfo.settings);
		inputInfo.selectedCardGui = null;
	}

	private GameEvent playCardWithTarget(Vector2f rootGuiDimensions) {
		inputInfo.selectedCardGui.setTargetPos(rootGuiDimensions.x - inputInfo.settings.cardWidth() * 0.5f, 200);
		inputInfo.selectedCardGui.setLockTargetPos(true);
		inputInfo.selectedCardGui.setLockPos(false);
		inputInfo.selectedCardGui.unhover(inputInfo.settings);
		inputInfo.cardWaitingForTarget = inputInfo.selectedCardGui;
		inputInfo.selectedCardGui = null;
		return null;
	}

	private GameEvent playCardWithoutTarget() {
		return inputInfo.playCard(inputInfo.selectedCardGui.cardID(), null);
	}

}
