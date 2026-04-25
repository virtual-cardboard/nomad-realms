package nomadrealms.context.game.interaction;

import engine.common.math.Vector2f;
import engine.context.input.Mouse;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.render.ui.Camera;

public class InteractionState {

	public Camera camera;
	public CardPlayer localPlayer;
	public Mouse mouse;
	public Vector2f screenDimensions;
	public boolean showMap = false;
	public boolean showDebugInfo = false;

	public long lastMouseMovedTime = System.currentTimeMillis();
	public long lastOpacityUpdateTime = System.currentTimeMillis();
	public float actorTextOpacity = 1;

	public InteractionState(Camera camera, CardPlayer localPlayer, Mouse mouse, Vector2f screenDimensions) {
		this.camera = camera;
		this.localPlayer = localPlayer;
		this.mouse = mouse;
		this.screenDimensions = screenDimensions;
	}

	public void updateActorTextOpacity() {
		long currentTime = System.currentTimeMillis();
		float dt = (currentTime - lastOpacityUpdateTime) / 1000f;
		lastOpacityUpdateTime = currentTime;
		long idleTime = currentTime - lastMouseMovedTime;
		float targetOpacity;
		if (idleTime < 3000) {
			targetOpacity = 1;
		} else if (idleTime < 4000) {
			targetOpacity = 1 - (idleTime - 3000) / 1000f;
		} else {
			targetOpacity = 0;
		}
		if (actorTextOpacity < targetOpacity) {
			actorTextOpacity = Math.min(targetOpacity, actorTextOpacity + dt / 0.2f);
		} else {
			actorTextOpacity = targetOpacity;
		}
	}

}
