package nomadrealms.game.card.intent;

import nomadrealms.game.card.WorldCard;
import nomadrealms.game.world.World;

public class PlayCardStartIntent implements  Intent {

    private final WorldCard card;

    public PlayCardStartIntent(WorldCard card) {
        this.card = card;
    }

    @Override
    public void resolve(World world) {

    }

    public WorldCard getCard() {
        return card;
    }
}
