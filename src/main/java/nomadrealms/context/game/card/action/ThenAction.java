package nomadrealms.context.game.card.action;

import java.util.List;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public class ThenAction implements Action {

	private Action first;
	private CardExpression next;
	private Target target;
	private CardPlayer source;

	private boolean nextExecuted = false;
	private int postDelayCounter = 0;

	/**
	 * No-arg constructor for serialization.
	 */
	public ThenAction() {
	}

	public ThenAction(Action first, CardExpression next, Target target, CardPlayer source) {
		this.first = first;
		this.next = next;
		this.target = target;
		this.source = source;
	}

	@Override
	public void update(World world) {
		if (!first.isComplete()) {
			first.update(world);
		} else if (postDelayCounter < first.postDelay()) {
			postDelayCounter++;
		} else if (!nextExecuted) {
			List<Effect> effects = next.effects(world, target, source);
			world.addProcChain(new ProcChain(effects));
			nextExecuted = true;
		}
	}

	@Override
	public boolean isComplete() {
		return first.isComplete() && postDelayCounter >= first.postDelay() && nextExecuted;
	}

	@Override
	public void init(World world) {
		first.init(world);
	}

	@Override
	public int preDelay() {
		return first.preDelay();
	}

	@Override
	public int postDelay() {
		return 0;
	}

	@Override
	public ConstraintPair screenOffset(RenderingEnvironment re) {
		return first.screenOffset(re);
	}

}
