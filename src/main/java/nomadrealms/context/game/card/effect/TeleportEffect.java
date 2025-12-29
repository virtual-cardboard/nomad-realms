package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.world.map.area.Tile;

public class TeleportEffect extends Effect {

    private final Actor target;
    private final Tile tile;
    private final int delay;

    public TeleportEffect(Actor source, Actor target, Tile tile, int delay) {
        this.source(source);
        this.target = target;
        this.tile = tile;
        this.delay = delay;
    }

    @Override
    public void resolve() {
        target.tile(tile);
    }

}
