package context.game.visuals.renderer.actor;

import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.GameCamera;
import model.actor.Actor;

public abstract class ActorDisplayer<T extends Actor> {

	@SuppressWarnings("unchecked")
	public final void displayActor(Actor actor, GLContext glContext, Vector2f rootGuiDimensions, GameCamera camera) {
		display(glContext, (T) actor, rootGuiDimensions, camera);
	}

	public abstract void display(GLContext glContext, T actor, Vector2f rootGuiDimensions, GameCamera camera);

}
