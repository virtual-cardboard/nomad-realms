package nomadrealms.context.game.card.action;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public abstract class Action {

	protected Actor source;

	protected Action() {
	}

	public Action(Actor source) {
		this.source = source;
	}

	public Actor source() {
		return source;
	}

	public abstract void update(World world);

	public abstract boolean isComplete();

	public void init(World world) {
	}

	public abstract int preDelay();

	public abstract int postDelay();

	public ConstraintPair screenOffset(RenderingEnvironment re) {
		return new ConstraintPair(absolute(0), absolute(0));
	}

}
