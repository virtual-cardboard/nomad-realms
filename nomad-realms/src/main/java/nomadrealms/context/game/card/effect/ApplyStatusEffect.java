package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import engine.serialization.Derializable;

@Derializable
public class ApplyStatusEffect extends Effect {

    private final Target target;
    private final StatusEffect statusEffect;
    private final int count;

    public ApplyStatusEffect(Actor source, Target target, StatusEffect statusEffect, int count) {
        super(source);
        this.target = target;
        this.statusEffect = statusEffect;
        this.count = count;
    }

    @Override
    public void resolve(World world) {
        if (target instanceof Actor) {
            ((Actor) target).status().add(statusEffect, count);
        }
    }
	/**
	 * No-arg constructor for serialization.
	 */
	ApplyStatusEffect() {
		this.target = null;
		this.statusEffect = null;
		this.count = 0;
	}
}

