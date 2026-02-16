package nomadrealms.context.game.card.action;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class DelayedEffectAction extends Action {

	private CardExpression expression;
	private int preDelay;
	private int postDelay;
	private Target target;

	private boolean executed = false;

	/**
	 * No-arg constructor for serialization.
	 */
	private DelayedEffectAction() {
		super();
	}

	public DelayedEffectAction(CardExpression expression, int preDelay, int postDelay, Target target,
			CardPlayer source) {
		super(source);
		this.expression = expression;
		this.preDelay = preDelay;
		this.postDelay = postDelay;
		this.target = target;
	}

	@Override
	public void update(World world) {
		if (!executed) {
			List<Effect> effects = expression.effects(world, target, source);
			for (Effect effect : effects) {
				effect.source(source);
			}
			world.addProcChain(new ProcChain(effects));
			executed = true;
		}
	}

	@Override
	public boolean isComplete() {
		return executed;
	}

	@Override
	public int preDelay() {
		return preDelay;
	}

	@Override
	public int postDelay() {
		return postDelay;
	}

}
