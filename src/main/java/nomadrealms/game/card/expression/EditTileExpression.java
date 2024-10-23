package nomadrealms.game.card.expression;

import nomadrealms.game.card.intent.EditTileIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.tile.factory.TileType;

import java.util.List;

import static java.util.Collections.singletonList;

public class EditTileExpression implements CardExpression {

    private final TileType tileType;

    public EditTileExpression(TileType tileType) {
        this.tileType = tileType;
    }

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        if (isInRange(world, target, source, source.tile().range())) {
            return singletonList(new EditTileIntent(source, (Tile) target, tileType));
        }
        return List.of();
    }

}
