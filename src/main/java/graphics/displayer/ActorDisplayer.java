package graphics.displayer;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.renderer.ActorBodyPartRenderer;
import context.visuals.renderer.TextureRenderer;
import engine.common.math.Vector2f;
import graphics.displayable.ActorBodyPart;
import model.actor.Actor;
import model.state.GameState;

public abstract class ActorDisplayer<T extends Actor> {

	protected TextureRenderer textureRenderer;
	private ActorBodyPartRenderer actorBodyPartRenderer;

	protected List<ActorBodyPart> actorBodyParts = new ArrayList<>(2);

	private boolean init;

	private long actorID;

	public ActorDisplayer(long actorID) {
		this.actorID = actorID;
	}

	public final void doInit(ResourcePack resourcePack, GameState state) {
		init(resourcePack, state);
		init = true;
	}

	protected void init(ResourcePack resourcePack, GameState state) {
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		actorBodyPartRenderer = resourcePack.getRenderer("actor_body_part", ActorBodyPartRenderer.class);
	}

	public abstract void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha);

	protected final void displayBodyParts(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, T t, float alpha,
			Vector2f direction) {
		Vector2f position = t.screenPos(camera, s);
		actorBodyPartRenderer.render(s, actorBodyParts, position, direction);
	}

	protected final void addBodyPart(ActorBodyPart bodyPart) {
		actorBodyParts.add(bodyPart);
	}

	public final boolean initialized() {
		return init;
	}

	public final long actorID() {
		return actorID;
	}

}
