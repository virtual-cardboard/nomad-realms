package nomadrealms.context.game.card.expression;

import java.util.List;
import java.util.stream.Collectors;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class DamageActorsExpression implements CardExpression {

    private final Query<CardPlayer> actorsQuery;
    private final int amount;

    public DamageActorsExpression(Query<CardPlayer> actorsQuery, int amount) {
        this.actorsQuery = actorsQuery;
        this.amount = amount;
    }

    @Override
    public List<Effect> effects(World world, Target target, CardPlayer source) {
        List<CardPlayer> actors = actorsQuery.find(world, source);
        return actors.stream()
                .map(actor -> new DamageEffect(actor, source, amount))
                .collect(Collectors.toList());
    }

}
