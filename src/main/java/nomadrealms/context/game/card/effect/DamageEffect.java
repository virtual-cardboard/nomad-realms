package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.HasHealth;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class DamageEffect extends Effect {

	private final Target target;
	private final Target source;
	private final int amount;

	public DamageEffect(Target target, Target source, int amount) {
		this.target = target;
		this.source = source;
		this.amount = amount;
	}

	@Override
	public void resolve(World world) {
		((HasHealth) target).damage(amount);
	}

}
