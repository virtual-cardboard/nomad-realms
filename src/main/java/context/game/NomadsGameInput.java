package context.game;

import java.util.List;

import common.math.Vector2i;
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
import context.input.GameInput;
import context.input.event.GameInputEvent;
import context.visuals.gui.Gui;
import context.visuals.gui.RootGui;
import networking.protocols.NomadRealmsProtocolDecoder;

public class NomadsGameInput extends GameInput {

	private NomadsInputWorldInfo worldInfo = new NomadsInputWorldInfo();
	private NomadsInputDeckBuildingInfo deckBuildingInfo = new NomadsInputDeckBuildingInfo();

//	private Vector2i previousCursorPos;
//	private boolean pressed = false;

	private NomadsGameVisuals visuals;

	@Override
	protected void init() {
		visuals = (NomadsGameVisuals) context().visuals();
		worldInfo.init(visuals, (NomadsGameData) context().data(), cursor());
		deckBuildingInfo.init(visuals, (NomadsGameData) context().data(), cursor());

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

	private Gui getGui(Class<?> clazz, Vector2i coords, RootGui rootGui) {
		float width = rootGui.widthPx();
		float height = rootGui.heightPx();
		List<Gui> children = rootGui.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Gui g = doGetGui(clazz, coords, children.get(i), 0, 0, width, height);
			if (g != null) {
				return g;
			}
		}
		return null;
	}

	private Gui doGetGui(Class<?> clazz, Vector2i coords, Gui gui, float pX, float pY, float pW, float pH) {
		if (!gui.isEnabled()) {
			return null;
		}
		float x = gui.posX().get(pX, pX + pW);
		float y = gui.posY().get(pY, pY + pH);
		float w = gui.width().get(pX, pX + pW);
		float h = gui.height().get(pY, pY + pH);
		List<Gui> children = gui.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Gui g = doGetGui(clazz, coords, children.get(i), x, y, w, h);
			if (g != null) {
				return g;
			}
		}
		if (clazz.isInstance(gui) && x <= coords.x && coords.x <= x + w && y <= coords.y && coords.y <= y + h) {
			return gui;
		}
		return null;
	}

}
