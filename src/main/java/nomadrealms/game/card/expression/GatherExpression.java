package nomadrealms.game.card.expression;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.intent.GatherIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.World;

import java.util.List;

import static java.util.Collections.singletonList;

public class GatherExpression implements CardExpression {

    private final int range;

    public GatherExpression(int range) {
        this.range = range;
    }

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        if (isInRange(world, target, source, range)) {
            return singletonList(new GatherIntent(source, source.tile(), range));
        }
        return List.of();
    }

}
