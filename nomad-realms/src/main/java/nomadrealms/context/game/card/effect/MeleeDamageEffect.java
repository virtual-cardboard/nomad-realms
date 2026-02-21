package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.action.MeleeAttackAction;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class MeleeDamageEffect extends Effect {

	private final Actor target;
	private final int amount;

	public MeleeDamageEffect(Target target, Actor source, int amount) {
		super(source);
		this.target = (Actor) target;
		this.amount = amount;
	}

	@Override
	public void resolve(World world) {
		source().queueAction(new MeleeAttackAction(source(), target, amount));
	}

	@Override
	public Target target() {
		return target;
	}

}
