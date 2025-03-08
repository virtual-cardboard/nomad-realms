package nomadrealms.game.card.expression;

import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.ReshuffleDecksIntent;
import nomadrealms.game.event.Target;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.world.World;

import java.util.Collections;
import java.util.List;

public class ReshuffleDecksExpression implements CardExpression {

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        return Collections.singletonList(new ReshuffleDecksIntent(source));
    }

}
