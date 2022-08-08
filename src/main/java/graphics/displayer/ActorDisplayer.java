package graphics.displayer;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.renderer.TextureRenderer;
import model.actor.Actor;
import model.id.Id;
import model.state.GameState;

public abstract class ActorDisplayer<T extends Actor> {

	protected TextureRenderer textureRenderer;

	private boolean init;

	private Id actorId;

	public ActorDisplayer() {
	}

	public ActorDisplayer(Id actorId) {
		this.actorId = actorId;
	}

	public final void doInit(ResourcePack resourcePack, GameState state) {
		init(resourcePack, state);
		init = true;
	}

	protected void init(ResourcePack resourcePack, GameState state) {
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
	}

	public abstract void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float dt);

	public final boolean initialized() {
		return init;
	}

	public final Id actorId() {
		return actorId;
	}

	public final void setActorId(Id actorId) {
		this.actorId = actorId;
	}

}
