package nomadrealms.game.card.expression;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.actor.structure.factory.StructureType;
import nomadrealms.game.card.intent.CreateStructureIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

import java.util.List;

import static java.util.Collections.singletonList;

public class CreateStructureExpression implements CardExpression {

    private final StructureType structureType;

    public CreateStructureExpression(StructureType structureType) {
        this.structureType = structureType;
    }

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        if (isInRange(world, target, source, source.tile().range())) {
            return singletonList(new CreateStructureIntent(source, (Tile) target, structureType));
        }
        return List.of();
    }

}
