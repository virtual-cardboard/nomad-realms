package nomadrealms.context.game.card.expression;

import java.util.List;
import java.util.stream.Collectors;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.FreezeEffect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class FreezeExpression implements CardExpression {

    private final Query<CardPlayer> targets;

    public FreezeExpression(Query<CardPlayer> targets) {
        this.targets = targets;
    }

    @Override
    public List<Effect> effects(World world, Target target, CardPlayer source) {
        return targets.find(world, source, target).stream()
                .map(FreezeEffect::new)
                .collect(Collectors.toList());
    }

}
