package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class RemoveStatusEffect extends Effect {

	private final Target target;
	private final StatusEffect statusEffect;
	private final int count;

	public RemoveStatusEffect(Actor source, Target target, StatusEffect statusEffect, int count) {
		super(source);
		this.target = target;
		this.statusEffect = statusEffect;
		this.count = count;
	}

	@Override
	public void resolve(World world) {
		if (target instanceof Actor) {
			((Actor) target).status().remove(statusEffect, count);
		}
	}
}
