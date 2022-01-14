package context.game.visuals.renderer;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.gui.RootGui;
import context.visuals.renderer.GameRenderer;
import model.state.GameState;

public class ActorRenderer extends GameRenderer {

	private ResourcePack resourcePack;

	public ActorRenderer(ResourcePack resourcePack) {
		this.resourcePack = resourcePack;
	}

	public ActorRenderer(GLContext glContext, ResourcePack resourcePack) {
		super(glContext);
		this.resourcePack = resourcePack;
	}

	public void renderActors(RootGui rootGui, NomadsSettings settings, GameState state, GameCamera camera, float alpha) {
		state.actors().values().stream().sorted((c1, c2) -> Float.compare(c1.screenPos(camera, settings).y, c2.screenPos(camera, settings).y)).forEach(c -> {
			if (!c.displayer().initialized()) {
				c.displayer().doInit(resourcePack);
			}
			c.displayer().display(glContext, settings, state, camera, alpha);
		});
		// TODO render other actors
	}

}
