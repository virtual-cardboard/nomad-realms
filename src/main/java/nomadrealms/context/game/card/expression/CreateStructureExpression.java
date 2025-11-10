package nomadrealms.context.game.card.expression;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.structure.factory.StructureType;
import nomadrealms.context.game.card.intent.CreateStructureIntent;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

import java.util.List;

import static java.util.Collections.singletonList;

public class CreateStructureExpression implements CardExpression {

    private final StructureType structureType;

    public CreateStructureExpression(StructureType structureType) {
        this.structureType = structureType;
    }

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        return singletonList(new CreateStructureIntent(source, (Tile) target, structureType));
    }

}
