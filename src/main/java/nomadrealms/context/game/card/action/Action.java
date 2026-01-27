package nomadrealms.context.game.card.action;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public interface Action {

	public void update(World world);

	public boolean isComplete();

	public default void init(World world) {
	}

	public int preDelay();

	public int postDelay();

	public default ConstraintPair screenOffset(RenderingEnvironment re) {
		return new ConstraintPair(absolute(0), absolute(0));
	}

}
