package nomadrealms.context.game.card.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.action.ThenAction;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class ThenExpression implements CardExpression {

	private CardExpression expr1;
	private CardExpression expr2;

	/**
	 * No-arg constructor for serialization.
	 */
	public ThenExpression() {
	}

	public ThenExpression(CardExpression expr1, CardExpression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		List<Effect> e1 = expr1.effects(world, target, source);
		if (e1.isEmpty()) {
			return expr2.effects(world, target, source);
		}
		ThenState state = new ThenState(expr2, target, source);
		List<Effect> result = new ArrayList<>();
		for (int i = 0; i < e1.size(); i++) {
			result.add(new ThenEffect(e1.get(i), state, i == e1.size() - 1, source));
		}
		return result;
	}

}

class ThenState {
	CardExpression next;
	Target target;
	CardPlayer source;
	Action lastAction;
	boolean nextHandled = false;

	public ThenState(CardExpression next, Target target, CardPlayer source) {
		this.next = next;
		this.target = target;
		this.source = source;
	}
}

class ThenEffect extends Effect {
	private Effect delegate;
	private ThenState state;
	private boolean isLast;

	public ThenEffect(Effect delegate, ThenState state, boolean isLast, CardPlayer source) {
		this.delegate = delegate;
		this.state = state;
		this.isLast = isLast;
		this.source(source);
	}

	@Override
	public void resolve(World world) {
		if (!(source() instanceof CardPlayer)) {
			delegate.resolve(world);
			if (isLast) {
				if (!state.nextHandled) {
					world.addProcChain(new ProcChain(state.next.effects(world, state.target, null)));
					state.nextHandled = true;
				}
			}
			return;
		}
		CardPlayer source = (CardPlayer) source();
		Consumer<Action> originalQueuer = source.actionQueuer();
		source.actionQueuer(action -> {
			if (state.lastAction != null) {
				originalQueuer.accept(state.lastAction);
			}
			state.lastAction = action;
		});
		try {
			delegate.resolve(world);
		} finally {
			source.actionQueuer(originalQueuer);
		}
		if (isLast) {
			if (state.lastAction != null) {
				source.queueAction(new ThenAction(state.lastAction, state.next, state.target, source));
			} else if (!state.nextHandled) {
				world.addProcChain(new ProcChain(state.next.effects(world, state.target, source)));
			}
			state.nextHandled = true;
		}
	}

	@Override
	public Effect source(Actor source) {
		delegate.source(source);
		return super.source(source);
	}
}
