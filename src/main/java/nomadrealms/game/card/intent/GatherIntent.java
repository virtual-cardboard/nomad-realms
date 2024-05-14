package nomadrealms.game.card.intent;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.tile.Tile;

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
