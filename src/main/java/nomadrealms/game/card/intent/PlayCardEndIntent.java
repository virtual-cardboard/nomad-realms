package nomadrealms.game.card.intent;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.world.World;

public class PlayCardEndIntent implements  Intent {

    private final CardPlayer source;
    private final WorldCard card;

    public PlayCardEndIntent(CardPlayer source, WorldCard card) {
        this.source = source;
        this.card = card;
    }

    @Override
    public void resolve(World world) {

    }

    public CardPlayer source() {
        return source;
    }

    public WorldCard card() {
        return card;
    }

}
