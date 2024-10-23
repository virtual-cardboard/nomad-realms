package nomadrealms.game.card.intent;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

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
        for (Tile t : world.getTilesInRange(tile, range)) {
            while (!t.items().isEmpty()) {
                WorldItem item = t.items().get(0);
                t.removeItem(item);
                source.inventory().add(item);
            }
        }
    }

}
