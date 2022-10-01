package context.game.visuals.renderer;

import static context.game.visuals.GameCamera.RENDER_RADIUS;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.NomadsGameData;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.TextureRenderer;
import engine.common.math.Vector2i;
import model.actor.Actor;
import model.state.GameState;

public class ActorRenderer extends GameRenderer {

	private final ResourcePack resourcePack;
	private final Texture actorAura;
	private final TextureRenderer textureRenderer;

	public ActorRenderer(GLContext glContext, ResourcePack resourcePack) {
		super(glContext);
		this.resourcePack = resourcePack;
		actorAura = resourcePack.getTexture("actor_aura");
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
	}

	public void renderActors(NomadsGameData data, GameState state, GameCamera camera, float dt) {
		NomadsSettings settings = data.settings();
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
				.sorted((c1, c2) -> Float.compare(
						c1.worldPos().screenPos(camera, settings).y(),
						c2.worldPos().screenPos(camera, settings).y()))
				.forEach(c -> {
					if (c.displayer() == null) {
						throw new IllegalStateException("Actor " + c + " has no displayer!");
					}
					if (!c.displayer().initialized()) {
						c.displayer().doInit(resourcePack, state);
					}
//					if (c.id().equals(data.playerID())) {
//						// Display actorAura
//						Vector2f screenPos = c.worldPos().screenPos(camera, settings);
//						float texWidth = actorAura.width() * settings.actorScale;
//						float texHeight = actorAura.height() * settings.actorScale;
//						textureRenderer.render(actorAura, screenPos.x() - texWidth / 2, screenPos.y() - texHeight / 2, texWidth, texHeight);
//					}
					c.displayer().display(glContext, settings, state, camera, dt);
				});
	}

}
