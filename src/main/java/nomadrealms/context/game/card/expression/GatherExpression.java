package nomadrealms.context.game.card.expression;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.intent.GatherIntent;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

import java.util.List;

import static java.util.Collections.singletonList;

public class GatherExpression implements CardExpression {

    private final int range;

    public GatherExpression(int range) {
        this.range = range;
    }

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        return singletonList(new GatherIntent(source, source.tile(), range));
    }

}