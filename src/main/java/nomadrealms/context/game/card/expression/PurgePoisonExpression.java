package nomadrealms.context.game.card.expression;

import static nomadrealms.context.game.actor.status.StatusEffect.POISON;

import java.util.List;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.ApplyStatusEffect;
import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class PurgePoisonExpression implements CardExpression {

    private final Query<Integer> amount;

    public PurgePoisonExpression(Query<Integer> amount) {
        this.amount = amount;
    }

    @Override
    public List<Effect> effects(World world, Target target, CardPlayer source) {
        int value = amount.find(world, source, target).stream().findFirst().orElse(0);
        return List.of(
                new DamageEffect(target, source, value),
                new ApplyStatusEffect(target, POISON, -value)
        );
    }
}
