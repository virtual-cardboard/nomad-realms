package nomadrealms.context.game.card.expression;

import java.util.List;
import java.util.stream.Collectors;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.intent.DamageIntent;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class DamageExpression implements CardExpression {

    private final int amount;
    private final Query<? extends Target> query;

    public DamageExpression(int amount) {
        this.amount = amount;
        this.query = null;
    }

    public DamageExpression(Query<? extends Target> query, int amount) {
        this.amount = amount;
        this.query = query;
    }

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        if (query != null) {
            return query.find(world, source).stream()
                    .map(t -> new DamageIntent(t, source, amount))
                    .collect(Collectors.toList());
        }
        return List.of(new DamageIntent(target, source, amount));
    }

}
