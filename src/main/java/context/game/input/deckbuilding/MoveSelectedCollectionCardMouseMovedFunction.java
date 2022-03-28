package context.game.input.deckbuilding;

import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import common.math.Vector3f;
import context.game.visuals.gui.deckbuilding.CollectionCardGui;
import context.input.event.MouseMovedInputEvent;

public class MoveSelectedCollectionCardMouseMovedFunction implements Function<MouseMovedInputEvent, GameEvent> {

	private NomadsInputDeckBuildingInfo inputInfo;

	public MoveSelectedCollectionCardMouseMovedFunction(NomadsInputDeckBuildingInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MouseMovedInputEvent t) {
		CollectionCardGui selectedCardGui = inputInfo.selectedCardGui;
		if (selectedCardGui != null) {
			selectedCardGui.setCenterPos(inputInfo.cursor.pos().toVec2f().sub(inputInfo.cardMouseOffset));
			Vector2f velocity = inputInfo.cursor.velocity().toVec2f();
			Vector3f perpendicular = new Vector3f(velocity.y, -velocity.x, 0);
			float rotateAmount = Math.min(40, velocity.length() * 0.3f);
			selectedCardGui.setCurrentOrientation(selectedCardGui.currentOrientation().rotateBy(perpendicular, rotateAmount));
		}
		return null;
	}

}
