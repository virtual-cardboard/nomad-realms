package context.game.visuals.renderer;

import static context.game.visuals.GameCamera.RENDER_RADIUS;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.renderer.GameRenderer;
import engine.common.math.Vector2i;
import model.actor.Actor;
import model.state.GameState;

public class ActorRenderer extends GameRenderer {

	private final ResourcePack resourcePack;

	public ActorRenderer(ResourcePack resourcePack) {
		this.resourcePack = resourcePack;
	}

	public ActorRenderer(GLContext glContext, ResourcePack resourcePack) {
		super(glContext);
		this.resourcePack = resourcePack;
	}

	public void renderActors(NomadsSettings settings, GameState state, GameCamera camera, float alpha) {
		List<Actor> allActors = new ArrayList<>();
		Vector2i cameraChunkPos = camera.chunkPos();
		for (int cy = -RENDER_RADIUS; cy <= RENDER_RADIUS; cy++) {
			for (int cx = -RENDER_RADIUS; cx <= RENDER_RADIUS; cx++) {
				List<Actor> actors = state.chunkToActors().get(cameraChunkPos.add(cx, cy));
				if (actors != null) {
					allActors.addAll(actors);
				}
			}
		}
		allActors.stream()
				.filter(actor -> !actor.shouldRemove())
				.sorted((c1, c2) -> Float.compare(c1.worldPos().screenPos(camera, settings).y(), c2.worldPos().screenPos(camera, settings).y()))
				.forEach(c -> {
					if (c.displayer() == null) {
						throw new IllegalStateException("Actor " + c + " has no displayer!");
					}
					if (!c.displayer().initialized()) {
						c.displayer().doInit(resourcePack, state);
					}
					c.displayer().display(glContext, settings, state, camera, alpha);
				});
	}

}
