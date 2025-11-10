package nomadrealms.context.game.card.intent;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class GatherIntent implements Intent {


    private final CardPlayer source;
    private final Tile tile;
    private final int range;

    public GatherIntent(CardPlayer source, Tile tile, int range) {
        this.source = source;
        this.tile = tile;
        this.range = range;
    }

    @Override
    public void resolve(World world) {
        System.out.println("Ranged harvest not yet implemented");
        while (!tile.items().isEmpty()) {
            WorldItem item = tile.items().get(0);
            tile.removeItem(item);
            source.inventory().add(item);
        }
    }

}
