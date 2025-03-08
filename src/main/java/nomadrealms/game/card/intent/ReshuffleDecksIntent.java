package nomadrealms.game.card.intent;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.world.World;
import nomadrealms.game.zone.Deck;

public class ReshuffleDecksIntent implements Intent {

    private final CardPlayer source;

    public ReshuffleDecksIntent(CardPlayer source) {
        this.source = source;
    }

    @Override
    public void resolve(World world) {
        for (Deck deck : source.deckCollection().decks()) {
            deck.shuffle();
        }
    }

    public CardPlayer source() {
        return source;
    }
}
