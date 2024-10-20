package nomadrealms.game.card.expression;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.intent.DestroyStructureIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

import java.util.List;

import static java.util.List.of;

public class DestroyStructureExpression implements CardExpression {

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        return of(new DestroyStructureIntent((Tile) target));
    }

}
