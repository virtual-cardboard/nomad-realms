package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.map.area.Tile;

public class GatherEffect extends Effect {

    private final CardPlayer target;
    private final Tile tile;
    private final int range;

    public GatherEffect(Actor source, CardPlayer target, Tile tile, int range) {
        this.source(source);
        this.target = target;
        this.tile = tile;
        this.range = range;
    }

    @Override
    public void resolve() {
        // TODO: Ranged harvest not yet implemented
        while (!tile.items().isEmpty()) {
            WorldItem item = tile.items().get(0);
            tile.removeItem(item);
            target.inventory().add(item);
        }
    }

}
