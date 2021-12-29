package context.game.visuals.displayer;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayable.ActorBodyPart;
import context.game.visuals.renderer.ActorBodyPartRenderer;
import context.visuals.renderer.TextureRenderer;
import model.actor.Actor;
import model.state.GameState;

public abstract class ActorDisplayer<T extends Actor> {

	protected TextureRenderer textureRenderer;
	private ActorBodyPartRenderer actorBodyPartRenderer;

	protected List<ActorBodyPart> actorBodyParts = new ArrayList<>(2);

	private boolean init;

	public final void doInit(ResourcePack resourcePack) {
		init(resourcePack);
		init = true;
	}

	protected void init(ResourcePack resourcePack) {
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		actorBodyPartRenderer = resourcePack.getRenderer("actor_body_part", ActorBodyPartRenderer.class);
	}

	public abstract void display(GLContext glContext, Vector2f screenDim, NomadsSettings s, GameState state, GameCamera camera, float alpha);

	protected final void displayBodyParts(GLContext glContext, Vector2f screenDim, NomadsSettings s, GameState state, GameCamera camera, T t,
			float alpha,
			Vector2f direction) {
		Vector2f position = t.screenPos(camera, s).add(t.velocity().scale(alpha * s.worldScale));
		actorBodyPartRenderer.render(glContext, screenDim, s, actorBodyParts, position, direction);
	}

	protected final void addBodyPart(ActorBodyPart bodyPart) {
		actorBodyParts.add(bodyPart);
	}

	public final boolean initialized() {
		return init;
	}

}
