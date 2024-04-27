package nomadrealms.game.event;

import nomadrealms.game.card.WorldCard;
import nomadrealms.game.world.actor.CardPlayer;
import nomadrealms.game.world.map.World;

public class CardPlayedEvent extends InputEvent {

    WorldCard worldCard;
    CardPlayer source;
    Target target;

    public CardPlayedEvent(WorldCard worldCard, CardPlayer source, Target target) {
        this.worldCard = worldCard;
        this.source = source;
        this.target = target;
    }

    public WorldCard card() {
        return worldCard;
    }

    public CardPlayer source() {
        return source;
    }

    public Target target() {
        return target;
    }

    @Override
    public void resolve(World world) {
        world.resolve(this);
    }

    @Override
    public String toString() {
        return "CardPlayedEvent{" +
                "card=" + worldCard.card() +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
