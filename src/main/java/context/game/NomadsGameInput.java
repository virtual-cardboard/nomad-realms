package context.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;

import context.GuiInput;
import context.game.input.ShowDeckBuildingWorkbenchKeyPressedFunction;
import context.game.input.deckbuilding.DetectHoveredCollectionCardMouseMovedFunction;
import context.game.input.deckbuilding.MoveSelectedCollectionCardMouseMovedFunction;
import context.game.input.deckbuilding.NomadsInputDeckBuildingInfo;
import context.game.input.deckbuilding.ReleaseCollectionCardMouseReleasedFunction;
import context.game.input.deckbuilding.SelectCollectionCardMousePressedFunction;
import context.game.input.world.CancelCardMousePressedFunction;
import context.game.input.world.CardTargetMousePressedFunction;
import context.game.input.world.DetectHoveredCardMouseMovedFunction;
import context.game.input.world.DetectPlayedCardMouseReleasedFunction;
import context.game.input.world.MoveSelectedCardMouseMovedFunction;
import context.game.input.world.NomadsInputWorldInfo;
import context.game.input.world.ResetCardPositionsFrameResizedFunction;
import context.game.input.world.SelectCardMousePressedFunction;
import context.input.event.GameInputEvent;
import graphics.gui.debugui.RollingAverageStat;
import networking.protocols.NomadRealmsProtocolDecoder;

public class NomadsGameInput extends GuiInput {

	private NomadsInputWorldInfo worldInfo = new NomadsInputWorldInfo();
	private NomadsInputDeckBuildingInfo deckBuildingInfo = new NomadsInputDeckBuildingInfo();

//	private Vector2i previousCursorPos;
//	private boolean pressed = false;

	private NomadsGameVisuals visuals;
	private NomadsGameData data;

	@Override
	protected void init() {
		visuals = (NomadsGameVisuals) context().visuals();
		data = (NomadsGameData) context().data();
		worldInfo.init(visuals, data, cursor());
		deckBuildingInfo.init(visuals, data, cursor());

		super.initGuiFunctions();
//		addMousePressedFunction(this::handleMousePressed);
//		addMouseReleasedFunction(this::handleMouseReleased);
//		addMouseMovedFunction((e) -> pressed, this::handleMouseMoved, true);
		addPacketReceivedFunction(new NomadRealmsProtocolDecoder());

		addKeyPressedFunction(new ShowDeckBuildingWorkbenchKeyPressedFunction(visuals));

		// Normal gameplay input functions
		addMouseMovedFunction(this::deckBuildingGuiDisabled, new DetectHoveredCardMouseMovedFunction(worldInfo), false);
		addMouseMovedFunction(this::deckBuildingGuiDisabled, new MoveSelectedCardMouseMovedFunction(worldInfo), false);
		addMousePressedFunction(this::deckBuildingGuiDisabled, new SelectCardMousePressedFunction(worldInfo), false);
		addMouseReleasedFunction(this::deckBuildingGuiDisabled, new DetectPlayedCardMouseReleasedFunction(worldInfo), false);
		addMousePressedFunction(this::deckBuildingGuiDisabled, new CardTargetMousePressedFunction(worldInfo), false);
		addMousePressedFunction(this::deckBuildingGuiDisabled, new CancelCardMousePressedFunction(worldInfo), false);
		addFrameResizedFunction(new ResetCardPositionsFrameResizedFunction(worldInfo));

		// Deck building input functions
		addMouseMovedFunction(this::deckBuildingGuiEnabled, new DetectHoveredCollectionCardMouseMovedFunction(deckBuildingInfo), false);
		addMouseMovedFunction(this::deckBuildingGuiEnabled, new MoveSelectedCollectionCardMouseMovedFunction(deckBuildingInfo), false);
		addMousePressedFunction(this::deckBuildingGuiEnabled, new SelectCollectionCardMousePressedFunction(deckBuildingInfo), false);
		addMouseReleasedFunction(this::deckBuildingGuiEnabled, new ReleaseCollectionCardMouseReleasedFunction(deckBuildingInfo), false);
//		addMousePressedFunction(this::deckBuildingGuiEnabled, new CardTargetMousePressedFunction(worldInfo), false);

		// Debug input functions
		addKeyPressedFunction(e -> {
			if (e.code() == GLFW_KEY_F3) {
				RollingAverageStat rollingAverageStat = data.rollingAverageStat();
				rollingAverageStat.setEnabled(!rollingAverageStat.isEnabled());
			}
			return null;
		});
	}

	private boolean deckBuildingGuiEnabled(GameInputEvent event) {
		return visuals.deckBuildingGui().isEnabled();
	}

	private boolean deckBuildingGuiDisabled(GameInputEvent event) {
		return !visuals.deckBuildingGui().isEnabled();
	}

//	private GameEvent handleMousePressed(MousePressedInputEvent event) {
//		pressed = true;
//		previousCursorPos = cursor().pos();
//		return null;
//	}
//
//	private GameEvent handleMouseReleased(MouseReleasedInputEvent mouse)releasedinputevent1) {
//		pressed = false;
//		return null;
//	}
//
//	private GameEvent handleMouseMoved(MouseMovedInputEvent event) {
//		Vector2f prevCameraPos = visuals.camera().pos();
//		Vector2i cursorPos = cursor().pos();
//		visuals.camera().setPos(prevCameraPos.add(previousCursorPos).sub(cursorPos));
//		previousCursorPos = cursorPos;
//		return null;
//	}

}
