package nomadrealms.context.game.zone;

import static nomadrealms.context.game.card.GameCard.ELECTROSTATIC_ZAPPER;
import static nomadrealms.context.game.card.GameCard.GATHER;
import static nomadrealms.context.game.card.GameCard.HEAL;
import static nomadrealms.context.game.card.GameCard.MELEE_ATTACK;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static nomadrealms.context.game.card.GameCard.WOODEN_CHEST;

import nomadrealms.context.game.card.WorldCard;

public enum BeginnerDecks {

    DEFAULT("Default"),
    FIGHTER("Fighter");

    private final String name;
    private final DeckCollection deckCollection;

    BeginnerDecks(String name) {
        this.name = name;
        this.deckCollection = new DeckCollection();
        if (this == DEFAULT) {
            initializeDefaultDecks(deckCollection);
        } else if (this == FIGHTER) {
            initializeFighterDecks(deckCollection);
        }
    }

    private void initializeDefaultDecks(DeckCollection collection) {
        for (Deck deck : collection.decks()) {
            deck
                    .addCard(new WorldCard(MOVE))
                    .addCard(new WorldCard(HEAL))
                    .addCard(new WorldCard(ELECTROSTATIC_ZAPPER))
                    .addCard(new WorldCard(WOODEN_CHEST).ephemeral(true))
                    .addCard(new WorldCard(MELEE_ATTACK))
                    .addCard(new WorldCard(GATHER));
            deck.shuffle();
        }
    }

    private void initializeFighterDecks(DeckCollection collection) {
        for (Deck deck : collection.decks()) {
            deck
                    .addCard(new WorldCard(MOVE))
                    .addCard(new WorldCard(MOVE))
                    .addCard(new WorldCard(MELEE_ATTACK))
                    .addCard(new WorldCard(MELEE_ATTACK))
                    .addCard(new WorldCard(MELEE_ATTACK));
            deck.shuffle();
        }
    }

    public DeckCollection deckCollection() {
        return deckCollection;
    }

    public String deckName() {
        return name;
    }
}
