package nomadrealms.render.ui.constraint;

import engine.visuals.constraint.Constraint;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.render.RenderingEnvironment;

public class ActorScreenPositionConstraint implements Constraint {

	private final Actor actor;
	private final RenderingEnvironment re;
	private final boolean isX;

	public ActorScreenPositionConstraint(Actor actor, RenderingEnvironment re, boolean isX) {
		this.actor = actor;
		this.re = re;
		this.isX = isX;
	}

	@Override
	public float get() {
		if (isX) {
			return actor.getScreenPosition(re).x();
		} else {
			return actor.getScreenPosition(re).y();
		}
	}
}
