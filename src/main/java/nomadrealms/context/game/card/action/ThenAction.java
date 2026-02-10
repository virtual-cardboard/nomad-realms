package nomadrealms.context.game.card.action;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public class ThenAction implements Action {

	private CardExpression expression1;
	private CardExpression expression2;
	private Target target;
	private CardPlayer source;

	private DelayedEffectAction delegate;
	private boolean queuedNext = false;

	public ThenAction() {
	}

	public ThenAction(CardExpression expression1, CardExpression expression2, Target target, CardPlayer source) {
		this.expression1 = expression1;
		this.expression2 = expression2;
		this.target = target;
		this.source = source;
		this.delegate = new DelayedEffectAction(expression1, 0, 0, target, source);
	}

	@Override
	public void update(World world) {
		delegate.update(world);
		if (delegate.isComplete() && !queuedNext) {
			source.queueAction(new DelayedEffectAction(expression2, 0, 0, target, source));
			queuedNext = true;
		}
	}

	@Override
	public boolean isComplete() {
		return delegate.isComplete();
	}

	@Override
	public int preDelay() {
		return delegate.preDelay();
	}

	@Override
	public int postDelay() {
		return delegate.postDelay();
	}

	@Override
	public ConstraintPair screenOffset(RenderingEnvironment re) {
		return delegate.screenOffset(re);
	}

}
