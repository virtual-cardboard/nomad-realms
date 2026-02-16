package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class ApplyStatusEffect extends Effect {

    private final Target target;
    private final StatusEffect statusEffect;
    private final int count;

    public ApplyStatusEffect(Target target, StatusEffect statusEffect, int count) {
        this.target = target;
        this.statusEffect = statusEffect;
        this.count = count;
        if (target instanceof Actor) {
            source((Actor) target);
        }
    }

    public ApplyStatusEffect(Target target, Actor source, StatusEffect statusEffect, int count) {
        this.target = target;
        this.statusEffect = statusEffect;
        this.count = count;
        source(source);
    }

    @Override
    public void resolve(World world) {
        if (target instanceof Actor) {
            ((Actor) target).status().add(statusEffect, count);
        }
    }
}
