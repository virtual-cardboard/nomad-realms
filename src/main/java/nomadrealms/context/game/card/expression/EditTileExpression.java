package nomadrealms.context.game.card.expression;

import nomadrealms.context.game.card.intent.EditTileIntent;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.tile.factory.TileType;

import java.util.List;

import static java.util.Collections.singletonList;

public class EditTileExpression implements CardExpression {

    private final TileType tileType;

    public EditTileExpression(TileType tileType) {
        this.tileType = tileType;
    }

    @Override
    public List<Intent> intents(World world, Target target, CardPlayer source) {
        return singletonList(new EditTileIntent(source, (Tile) target, tileType));
    }

}
