package nomadrealms.context.game.interaction;

import engine.common.time.PerformanceProfiler;
import engine.context.input.Mouse;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.render.ui.Camera;
import nomadrealms.user.Player;

/**
 * Holds transient, client-side data that doesn't belong in the GameState (persistent save data) or the
 * RenderingEnvironment (shaders and hardware-level renderers).
 */
public class InteractionState {

	public Camera camera = new Camera(0, 0);
	public Mouse mouse;

	public final ConstraintBox worldViewport;

	public boolean showDebugInfo = false;
	public boolean showMap = false;

	public long lastMouseMovedTime = System.currentTimeMillis();
	public long lastOpacityUpdateTime = System.currentTimeMillis();
	public float actorTextOpacity = 1;

	public Player localPlayer;
	private PerformanceProfiler profiler;

	public InteractionState(Mouse mouse, ConstraintBox screen) {
		this.mouse = mouse;
		this.worldViewport = new ConstraintBox(
				screen.x(),
				screen.y(),
				screen.w().multiply(0.6f),
				screen.h()
		);
	}

	public PerformanceProfiler profiler() {
		if (profiler == null) {
			profiler = new PerformanceProfiler(100);
		}
		return profiler;
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
