package context.game.visuals.renderer;

import app.NomadsSettings;
import context.GLContext;
import context.game.visuals.GameCamera;
import context.visuals.gui.RootGui;
import context.visuals.renderer.GameRenderer;
import model.state.GameState;

public class ActorRenderer extends GameRenderer {

	public ActorRenderer() {
	}

	public ActorRenderer(GLContext glContext) {
		super(glContext);
	}

	public void renderActors(RootGui rootGui, NomadsSettings settings, GameState state, GameCamera camera, float alpha) {
		state.actors().values().stream().sorted((c1, c2) -> Float.compare(c1.screenPos(camera, settings).y, c2.screenPos(camera, settings).y)).forEach(c -> {
			c.displayer().display(glContext, settings, state, camera, alpha);
		});
		// TODO render other actors
	}

}
