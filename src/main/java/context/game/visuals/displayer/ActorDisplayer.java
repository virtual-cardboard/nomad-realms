package context.game.visuals.displayer;

import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.GameCamera;
import model.actor.Actor;

public abstract class ActorDisplayer<T extends Actor> {

	public abstract void display(GLContext glContext, Vector2f rootGuiDimensions, GameCamera camera);

}
